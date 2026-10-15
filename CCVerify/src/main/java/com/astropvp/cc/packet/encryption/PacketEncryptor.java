package com.astropvp.cc.packet.encryption;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

public class PacketEncryptor {
    private final int[] c;
    private final int[] a;
    private final boolean b;

    public PacketEncryptor(byte[] var1, boolean var2) {
        this(ByteBuffer.wrap(var1), var2);
    }

    public PacketEncryptor(ByteBuffer var1, boolean var2) {
        if (var2) {
            if (var1.remaining() != 16 && var1.remaining() != 24) {
                throw new IllegalArgumentException("invalid key length: " + var1.remaining() + ", expected: " + 16 + " or " + 24);
            }
        } else if (var1.remaining() != 16) {
            throw new IllegalArgumentException("invalid key length: " + var1.remaining() + ", expected: " + 16);
        }

        var1.order(ByteOrder.BIG_ENDIAN);
        this.c = new int[]{var1.getInt(), var1.getInt(), var1.getInt(), var1.getInt()};
        if (var2 && var1.hasRemaining()) {
            this.a = new int[]{var1.getInt(), var1.getInt()};
        } else {
            this.a = null;
        }

        this.b = var2;
    }

    public int a() {
        int var1 = 16;
        if (this.a != null) {
            var1 += 8;
        }

        return var1;
    }

    public String b() {
        return Base64.getEncoder().encodeToString(this.c());
    }

    public byte[] c() {
        int var1 = this.a();
        byte[] var2 = new byte[var1];
        ByteBuffer var3 = ByteBuffer.wrap(var2);
        this.a(var3);
        return var2;
    }

    private void a(ByteBuffer var1) {
        var1.order(ByteOrder.BIG_ENDIAN);
        var1.putInt(this.c[0]);
        var1.putInt(this.c[1]);
        var1.putInt(this.c[2]);
        var1.putInt(this.c[3]);
        if (this.a != null) {
            var1.putInt(this.a[0]);
            var1.putInt(this.a[1]);
        }

    }

    public byte[] a(byte[] var1, boolean var2) {
        ByteBuffer var11 = this.b(ByteBuffer.wrap(var1), var2);
        byte[] var12 = new byte[var11.remaining()];
        var11.get(var12);
        return var12;
    }

    public ByteBuffer b(ByteBuffer var1, boolean var2) {
        int var10 = var1.remaining();
        int var11 = 0;
        if (var10 > 0) {
            var11 = 8 - (var10 & 7);
            if (!var2) {
                if (var11 != 8) {
                    throw new IllegalArgumentException("input not aligned to block size of 8");
                }

                var11 = 0;
            }
        }

        int var12 = var10 + var11;
        ByteBuffer var13 = var1.isDirect() ? ByteBuffer.allocateDirect(var12) : ByteBuffer.allocate(var12);
        var13.order(ByteOrder.BIG_ENDIAN);
        var13.put(var1.duplicate());

        int var14;
        for(var14 = 0; var14 < var11; ++var14) {
            var13.put((byte)var11);
        }

        var13.flip();
        var14 = 0;
        int var15 = 0;
        if (this.a != null) {
            var14 = this.a[0];
            var15 = this.a[1];
        }

        int var16 = var12 >>> 3;

        for(int var17 = 0; var17 < var16; ++var17) {
            int var18 = var17 << 3;
            int var19 = var13.getInt(var18);
            int var20 = var13.getInt(var18 + 4);
            if (this.b) {
                var19 ^= var14;
                var20 ^= var15;
            }

            int var21 = 0;

            for(int var22 = 0; var22 < 32; ++var22) {
                var19 += (var20 << 4 ^ var20 >>> 5) + var20 ^ var21 + this.c[var21 & 3];
                var21 -= 1640531527;
                var20 += (var19 << 4 ^ var19 >>> 5) + var19 ^ var21 + this.c[var21 >>> 11 & 3];
            }

            var14 = var19;
            var15 = var20;
            var13.putInt(var18, var19);
            var13.putInt(var18 + 4, var20);
        }

        return var13;
    }

    public byte[] b(byte[] var1, boolean var2) {
        ByteBuffer var11 = this.a(ByteBuffer.wrap(var1), var2);
        byte[] var12 = new byte[var11.remaining()];
        var11.get(var12);
        return var12;
    }

    public ByteBuffer a(ByteBuffer data, boolean verifyPad) {
        int inputDize = data.remaining();
        if ((inputDize & 7) != 0) {
            throw new IllegalArgumentException("input not aligned to block size of 8");
        } else {
            ByteBuffer workedData = data.isDirect() ? ByteBuffer.allocateDirect(inputDize) : ByteBuffer.allocate(inputDize);
            workedData.order(ByteOrder.BIG_ENDIAN);
            workedData.put(data.duplicate());
            workedData.flip();
            int var11 = 0;
            int var12 = 0;
            if (this.a != null) {
                var11 = this.a[0];
                var12 = this.a[1];
            }

            int var13 = inputDize >>> 3;

            int var14;
            int var16;
            int var17;
            for(var14 = 0; var14 < var13; ++var14) {
                int var15 = var14 << 3;
                var16 = workedData.getInt(var15);
                var17 = workedData.getInt(var15 + 4);
                int originalInt1 = var16;
                int originalInt2 = var17;
                int var20 = -957401312;

                for(int var21 = 0; var21 < 32; ++var21) {
                    var17 -= (var16 << 4 ^ var16 >>> 5) + var16 ^ var20 + this.c[var20 >>> 11 & 3];
                    var20 += 1640531527;
                    var16 -= (var17 << 4 ^ var17 >>> 5) + var17 ^ var20 + this.c[var20 & 3];
                }

                if (this.b) {
                    var16 ^= var11;
                    var17 ^= var12;
                }

                var11 = originalInt1;
                var12 = originalInt2;
                workedData.putInt(var15, var16);
                workedData.putInt(var15 + 4, var17);
            }

            if (verifyPad && inputDize > 0) {
                var14 = workedData.get(inputDize - 1) & 255;
                boolean var22 = false;
                if (var14 > 0 && var14 <= 8) {
                    for(var16 = 2; var16 <= var14; ++var16) {
                        var17 = workedData.get(inputDize - var16) & 255;
                        if (var14 != var17) {
                            var22 = true;
                            break;
                        }
                    }
                } else {
                    var22 = true;
                }

                if (var22) {
                    throw new IllegalArgumentException("invalid padding");
                }

                workedData.limit(workedData.limit() - var14);
                workedData = workedData.slice();
            }

            return workedData;
        }
    }
}
