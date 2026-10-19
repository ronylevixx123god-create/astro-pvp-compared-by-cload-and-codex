import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

/**
 * Read-only link check. For each plugin jar, reads the class files' constant pools (no plugin
 * code is run) and checks every class/field/method it uses from outside its own jar against a
 * simulated server classpath (WineSpigot + plugins). Classes are loaded with initialize=false,
 * so no static code runs. Writes a text report.
 *
 * args: pluginsFile (name|module|jar) classpathFile (one jar per line) reportFile
 */
public class LinkCheck3 {
    static ClassLoader server;     // WineSpigot only
    static ClassLoader world;      // WineSpigot + all plugins (server first)

    public static void main(String[] a) throws Exception {
        List<String> cp = Files.readAllLines(Paths.get(a[1]));
        List<URL> urls = new ArrayList<>();
        for (String p : cp) if (!p.trim().isEmpty()) urls.add(new File(p.trim()).toURI().toURL());
        server = new URLClassLoader(new URL[]{urls.get(0)}, null);
        world = new URLClassLoader(urls.toArray(new URL[0]), null);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(a[2]), StandardCharsets.UTF_8));
        int totalProblems = 0;
        for (String line : Files.readAllLines(Paths.get(a[0]))) {
            if (line.trim().isEmpty()) continue;
            String[] f = line.split("\\|");
            totalProblems += checkPlugin(f[0], f[2], out);
        }
        out.println("TOTAL PROBLEMS: " + totalProblems);
        out.close();
    }

    static int checkPlugin(String name, String jarPath, PrintWriter out) throws IOException {
        Set<String> own = new HashSet<>();
        List<byte[]> classes = new ArrayList<>();
        // Only classes compiled from the module's own source (<module>/target/classes) are checked;
        // bundled library copies still count as "own" for lookups.
        Path ownDir = Paths.get(jarPath).getParent().resolve("classes");
        try (ZipFile z = new ZipFile(jarPath)) {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                ZipEntry en = e.nextElement();
                if (!en.getName().endsWith(".class")) continue;
                own.add(en.getName().substring(0, en.getName().length() - 6));
                if (!Files.exists(ownDir.resolve(en.getName()))) continue;
                try (InputStream in = z.getInputStream(en)) { classes.add(readAll(in)); }
            }
        }
        // Classes the plugin also bundles but the server provides: the server copy wins at runtime.
        Set<String> refsChecked = new TreeSet<>();
        Map<String, String> problems = new TreeMap<>();
        for (byte[] c : classes) {
            List<String[]> refs;
            try { refs = ConstantPool.refs(c); } catch (Exception ex) { continue; }
            for (String[] r : refs) {
                String owner = r[1];
                if (owner.startsWith("[") || owner.startsWith("java/") || owner.startsWith("javax/") || owner.startsWith("sun/")) continue;
                boolean serverHas = server.getResource(owner + ".class") != null;
                if (!serverHas && own.contains(owner)) continue;
                String key = r[0] + " " + owner + (r[2] == null ? "" : "." + r[2] + ":" + r[3]);
                if (!refsChecked.add(key)) continue;
                String res = resolve(r[0], owner, r[2], r[3], serverHas ? server : world);
                if (res != null) problems.put(key, res);
            }
        }
        out.println("== " + name + " (" + jarPath + "): " + refsChecked.size() + " external references, " + problems.size() + " problems");
        for (Map.Entry<String, String> p : problems.entrySet()) out.println("   " + p.getValue() + "  " + p.getKey());
        out.flush();
        return problems.size();
    }

    /** @return null if OK, otherwise a problem label. */
    static String resolve(String kind, String owner, String member, String desc, ClassLoader cl) {
        Class<?> c;
        try {
            c = Class.forName(owner.replace('/', '.'), false, cl);
        } catch (ClassNotFoundException e) {
            return "MISSING_CLASS";
        } catch (LinkageError e) {
            return "CLASS_UNLOADABLE(" + e.getMessage() + ")";
        }
        if (member == null) return null;
        try {
            for (Class<?> k = c; k != null; k = k.getSuperclass()) if (matches(k, kind, member, desc)) return null;
            for (Class<?> i : interfaces(c)) if (matches(i, kind, member, desc)) return null;
            return "MISSING_MEMBER";
        } catch (LinkageError e) {
            return "UNVERIFIABLE(" + e.getMessage() + ")";
        }
    }

    static Set<Class<?>> interfaces(Class<?> c) {
        Set<Class<?>> s = new LinkedHashSet<>();
        for (Class<?> k = c; k != null; k = k.getSuperclass())
            for (Class<?> i : k.getInterfaces()) { s.add(i); s.addAll(interfaces(i)); }
        return s;
    }

    static boolean matches(Class<?> k, String kind, String name, String desc) {
        if (kind.equals("Field")) {
            for (Field f : k.getDeclaredFields()) if (f.getName().equals(name) && sig(f.getType()).equals(desc)) return true;
            return false;
        }
        if (name.equals("<init>")) {
            for (Constructor<?> m : k.getDeclaredConstructors())
                if (params(m.getParameterTypes(), "V").equals(desc) && !Modifier.isPrivate(m.getModifiers())) return true;
            return false;
        }
        for (Method m : k.getDeclaredMethods())
            if (m.getName().equals(name) && params(m.getParameterTypes(), sig(m.getReturnType())).equals(desc) && !Modifier.isPrivate(m.getModifiers())) return true;
        return false;
    }

    static String params(Class<?>[] ps, String ret) {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> p : ps) sb.append(sig(p));
        return sb.append(")").append(ret).toString();
    }

    static String sig(Class<?> t) {
        if (t.isArray()) return "[" + sig(t.getComponentType());
        if (!t.isPrimitive()) return "L" + t.getName().replace('.', '/') + ";";
        if (t == void.class) return "V"; if (t == boolean.class) return "Z"; if (t == int.class) return "I";
        if (t == long.class) return "J"; if (t == double.class) return "D"; if (t == float.class) return "F";
        if (t == short.class) return "S"; if (t == byte.class) return "B"; return "C";
    }

    static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        for (int n; (n = in.read(buf)) > 0; ) b.write(buf, 0, n);
        return b.toByteArray();
    }

    /** Minimal class-file constant pool reader: returns {kind, owner, name, descriptor}. */
    static final class ConstantPool {
        static List<String[]> refs(byte[] b) throws IOException {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(b));
            if (in.readInt() != 0xCAFEBABE) throw new IOException("not a class");
            in.readUnsignedShort(); in.readUnsignedShort();
            int n = in.readUnsignedShort();
            Object[] cp = new Object[n];
            int[] tag = new int[n];
            for (int i = 1; i < n; i++) {
                int t = in.readUnsignedByte();
                tag[i] = t;
                switch (t) {
                    case 1: cp[i] = in.readUTF(); break;
                    case 3: case 4: in.readInt(); break;
                    case 5: case 6: in.readLong(); i++; break;
                    case 7: case 8: case 16: case 19: case 20: cp[i] = in.readUnsignedShort(); break;
                    case 9: case 10: case 11: case 12: case 18: case 17:
                        cp[i] = new int[]{in.readUnsignedShort(), in.readUnsignedShort()}; break;
                    case 15: in.readUnsignedByte(); in.readUnsignedShort(); break;
                    default: throw new IOException("bad tag " + t);
                }
            }
            List<String[]> out = new ArrayList<>();
            for (int i = 1; i < n; i++) {
                if (tag[i] == 7) {
                    out.add(new String[]{"Class", (String) cp[(Integer) cp[i]], null, null});
                } else if (tag[i] == 9 || tag[i] == 10 || tag[i] == 11) {
                    int[] r = (int[]) cp[i];
                    String owner = (String) cp[(Integer) cp[r[0]]];
                    int[] nt = (int[]) cp[r[1]];
                    String kind = tag[i] == 9 ? "Field" : "Method";
                    out.add(new String[]{kind, owner, (String) cp[nt[0]], (String) cp[nt[1]]});
                }
            }
            return out;
        }
    }
}
