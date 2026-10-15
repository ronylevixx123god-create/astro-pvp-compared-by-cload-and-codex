package me.ifamasssxd.cosmicsky.inventorypets.struct;

import com.cosmicpvp.cosmicutils.nbt.*;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.mojang.authlib.*;
import com.mojang.authlib.properties.*;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.*;

public enum InventoryPets
{
   FEIGN_DEATH("FEIGN_DEATH", 2, CC.YellowB + "Feign Death Pet", CC.YellowB + "Feign Death Pet", new InventoryFeignDeathPet(), "eyJ0aW1lc3RhbXAiOjE1NTk2ODAwNTI5MDcsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NjBhMDE0ZDhjOTAyMzVmNzc1Y2Y5OTljY2M2ODM2NWQ3OGM2YTM2NjI0MDM5MjBlNjQ5NjM0MjZlY2IyNDc1In19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwODg1MTk1NywKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg3OWNlY2YyZWQ4YWZkNWI1Y2ZkZmVkNzdhMzRlNzUxNTVlMzI0YzA1MmRjN2Y2NzU1ZTEyOWRiODU1ZTgzNiIKICAgIH0KICB9Cn0="),
   BANNER("BANNER", 3, CC.YellowB + "Banner Pet", CC.YellowB + "Banner Pet", new InventoryBannerPet(), "eyJ0aW1lc3RhbXAiOjE1NjAyMTE1OTI3MzEsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xZTVlOTlhMjcwM2M2Y2YzYTQyNDU2Zjg3YzdlZTc5NzNiNzJmN2VhMDI1Njk4YzA2ZTVmZGVmOGE4NDhhZDllIn19fQ=="),
   GAIA("GAIA", 6, CC.YellowB + "Gaia Pet", CC.YellowB + "Gaia", new InventoryGaiaPet(), "eyJ0aW1lc3RhbXAiOjE1NjIwOTAxODY4OTYsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jY2UzYTc0MjdiY2IwNTRlZjFhNzY0NGY2Y2EwZWQwZWRjOTY3NDdhYWJjZjJlMWQ1ZjVmNjc3YjhkZGY2NDA3In19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwODc4ODkyOSwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk1MjZlZTU1ODU3Y2I3ZTk5ODMzOTFlNDhjZWM2MDYzMmY4MWZhMDkzN2RlZTQyZThlMDEzZmIyNDY2YWY1YSIKICAgIH0KICB9Cn0="),
   YIJKI_CONTROLLER("YIJKI_CONTROLLER", 11, CC.YellowB + "Yijki Controller Pet", CC.YellowB + "Yijki Controller", new InventoryYijkiControllerPet(), "eyJ0aW1lc3RhbXAiOjE1NjM4NDcwMzA3OTQsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hMWVkM2YzN2Q4ZTkwNjNkY2VlYTI0NTQ3M2U2ZDljYzk2ODliYzM0NWFlMjFlYTE1NTk4ZGU1NGE0NDY0N2Y4In19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwODcxMDI1NSwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNmYWZmN2I2YzQ3NGJkMDIwN2IyNGQwM2IxZTA2OWY4M2ZmZTAyNWJiZWIzMzdiMzdhNzk2NzAxNzI2NmRlMSIKICAgIH0KICB9Cn0="),
   WORLD_DESTROYER("WORLD_DESTROYER", 12, CC.YellowB + "World Destroyer Pet", CC.YellowB + "World Destroyer", new InventoryWorldDestroyerPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGJlNjMyNjc0NmQ3MWQzNTU5MzMxNjFlZDA3NjVkYWRlM2JjYzdiNDdkODRmN2JlN2ExMjk1NzNlMjI3NGViIn19fQ====", "eyJ0aW1lc3RhbXAiOjE1OTQ2OTg0NjYxMzYsInByb2ZpbGVJZCI6IjNmNjUzNTNiODlhMzQ5MzE4Nzc1MWI1NzM0YTA4NTUxIiwicHJvZmlsZU5hbWUiOiJIYWthR2FtYmEiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZTY1MTlkYjI0MjFmZTY2MmFkNWFhZmM1YjljMmM0NmM2ZGM5NGIwNjdlODNjNjA0MWEzMTg1MzZiN2Q2ZCJ9fX0="),
   ARCANE_CREEPER("ARCANE_CREEPER", 14, CC.YellowB + "Arcane Creeper Pet", CC.YellowB + "Arcane Creeper", new InventoryArcaneCreeperPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM3YzZmZTM0NGQ5NzBmYTI3MzRkYzQzNDYzZjkxYTYxYmM3N2I3Y2QxMzJkNThjYjg2MzI1OGEzMjc2YSJ9fX0="),
   SHACKLED_GOLEM("SHACKLED_GOLEM", 15, CC.YellowB + "Shackled Golem Pet", CC.YellowB + "Shackled Golem", new InventoryShackledGolem(), "eyJ0aW1lc3RhbXAiOjE1NzU5MzEzNDAwNjYsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84MGZkNzBmZWU2OGZmMTIwODcyNmMxMGU2ZDZiZjU2MWRjYjI5MWFhMDQ2MTgyMDQzMGM3NjE2ZDVkOGFhMjI1In19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwODEzNDQ3MCwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ4Mzg5YjE1NWE2ZDc0NDJhZDNhYmQzYmVmOTUzYTg3MTY4Y2FmMGI1ZGIxMjQ5MTQ1NjdlOTE1NDAxOGM4NyIKICAgIH0KICB9Cn0="),
   STITCHES("STITCHES", 16, CC.YellowB + "Stitches Pet", CC.YellowB + "Stitches", new InventoryStitchesPet(), "eyJ0aW1lc3RhbXAiOjE1NzIzMDU5MDY3NDgsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NTU1ZGY5NjkzOGMyYjc1MjI3YWYzYTVhOTI0MjdjZTU5MDFjNjhhZmE4OTBlYWVmOWNlM2QyMmU1MTFhZjFjIn19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNzk3NTg5MywKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiMWQxZjk3MzlhOTBlMjhjNjhhOGYxMTlkZDU2MzM0ZjIxYzhmMWUxN2MwNDc1OWQ0MzFmNjM0OWVhMWY5MiIKICAgIH0KICB9Cn0="),
   AETHER_FAIRY("AETHER_FAIRY", 17, CC.YellowB + "Aether Fairy Pet", CC.YellowB + "Aether Fairy", new InventoryFairyPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg3ZmNjY2FlNGM1NmIxOTY5ZTZkY2ZkZDdhOTkyMTY2N2ZmYjNjMjU5NjhjZDE4N2UwZDdiYTQ1YmFlMzdmYiJ9fX0=", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUzMTA5YWFmYzFiMTM3YWVhMTlhYmJhYmJmYzg3YTM2ZmM1MWY4N2Q5ZTBmNTkzYTNmMWZlZGEwZTE3ODRiNyJ9fX0="),
   DEMONIC_IMPLING("DEMONIC_IMPLING", 17, CC.YellowB + "Demonic Impling Pet", CC.YellowB + "Demonic Impling", new InventoryImplingPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFhYjRjZGFmNWIzNGZmZjEzODdjOWE2MzA4NzYyMTU4MmZlNzNiZGEyYTgzNmI2OGM1Y2ZmYzlhYTRmYzMifX19", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdhNTk0ZTAyMzFkM2Q0NzE5NWY4NGFlNTFiYjI5YWQ2NTEwZWE0M2M3MDI0MTk3YmEwOGZjNTQ1MWUxNWYxNyJ9fX0="),
   BANSHEE("BANSHEE", 19, CC.DarkRedB + "Banshee Pet", CC.DarkRed + "Banshee", new InventoryBansheePet(), "eyJ0aW1lc3RhbXAiOjE1Njc1NDcyOTgzODIsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MTU2OWUyZGRjNzcxODFhZDBkNGY4MzA5YzQ3MDU4M2M0MjI1ZTJlY2M3NWNlMzBjZjc2NTAxNDg5MzhmNDg4In19fQ=", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNTcwMDYxOSwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVmMzdjMjVmMjcyOTZlNTkzMTA0OTY3YzIzY2YwZTA5ZTJiMTRiZjMzNmQxZjI5YTYzYWE4ZTYwMmNiZDIzOSIKICAgIH0KICB9Cn0="),
   TITAN("TITAN", 20, CC.DarkRedB + "Titan Pet", CC.DarkRed + "Titan", new InventoryTitanPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJkZTFmYTcxZTgzMzRiOGExNTU1NWYyZTQ4NjBmZmQzZTU4ZDYxNDU3NzdjZWU5ZGM5ZTU5ZTdhMjNmYTEifX19=", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNzQyMjk4MSwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ1MzI1N2Q4MjdhYTA1YWVhZjU0NjVkMjI5ODM5OTJiYjg1NjU3MDNkYzNjOGY1NjkwMDVjYjI1M2UzYzNkMyIKICAgIH0KICB9Cn0"),
   SKRRT("SKRRT", 21, CC.DarkRedB + "Skrrt Pet", CC.DarkRedB + "Skrrt", new InventorySkrrtPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNmNjViNjEwZTZjMDY4M2M1NWJjMDkwNmQ0OWIyYWI0YzhhODVhYjM4ZDRkODVjZDRiMzNlNzMyMzg1YmFmMCJ9fX0", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNzQ5OTkxOSwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThhOGMzODg2YWY5ZGIyZTEwMzQ0MDk3OGIxYjRhNDE1Y2VhODk5ZjUwMDkzOTM5MDNjNDQ0MmE4MTc1ZTAyMCIKICAgIH0KICB9Cn0"),
   BLOODHOUND("BLOODHOUND", 22, CC.DarkRedB + "Bloodhound Pet", CC.DarkRedB + "Bloodhound", new InventoryBloodhoundPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE0NjU5M2YyNmY1MTgxZGI1MWQ1NDcyZDQ5NTg5NmQ4Mjc3N2UzMmRjZWFlMzgwYzA0NjY5YzlmYTdkNjZkIn19fQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNzU4NzkwNywKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIwNGZiMTJhOTI1MTE2N2UxYzRmMjRiMjY1NDY3ZDdjYjAzMWJkMjRjNWJkYTI5NDNmNzFlMzNkZmNkOTdmZiIKICAgIH0KICB9Cn0="),
   WITCHER("WITCHER", 23, CC.DarkRedB + "Witcher Pet", CC.DarkRedB + "Witcher", new InventoryWitcherPet(), "ewogICJ0aW1lc3RhbXAiIDogMTU4OTQyNjcxOTI1OCwKICAicHJvZmlsZUlkIiA6ICI3Yzg5NWFkMDExZDA0MzUwOWFlNWYyYmIxY2Y2YzhlYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYXNpY0JBRSIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84MTc4Y2YyYTY2MzFlNzlhODMyN2MyMTkyZGU2YTgzNjAzODQyZDlmZTNhMTE1ZGI5Y2JmOWJlZmE1NTZjYTNhIgogICAgfQogIH0KfQ==", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDcwNzY4Mjg2NCwKICAicHJvZmlsZUlkIiA6ICIzZjY1MzUzYjg5YTM0OTMxODc3NTFiNTczNGEwODU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYWthR2FtYmEiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFkNjc1MDZmYmVhZGU3MGI3YjhkODI4NjlkMGFkMTUxMDU5NTg2ZTZkZTQ2NjRmYzA1ZTZiMzNiZDQ1NjlkMiIKICAgIH0KICB9Cn0="),

   MUTATED_WORLD_DESTROYER("MUTATED_WORLD_DESTROYER", 24,CC.translate("&f&k;&r&2&l☣&f&k;&r &2● &2&l&nMutated&r &a&lWorld Destroyer Pet &2● &f&k;&r&2&l☣&f&k;&r"),
           CC.translate("&f&k;&r&2&l☣&f&k;&r &2● &2&l&nMutated&r &a&lWorld Destroyer Pet &2● &f&k;&r&2&l☣&f&k;&r"),
           new InventoryMutatedWorldDestroyerPet(),
           "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRlMmVhYTFiYmNhMWUwZThmYjllODgzOTIyYzNlMWRkZTI1YTI2Nzc1YTZmNTZjMThhOTA4YzdhYmQ1YjUwMyJ9fX0=====",
           "eyJ0aW1lc3RhbXAiOjE1OTQ2OTg0NjYxMzYsInByb2ZpbGVJZCI6IjNmNjUzNTNiODlhMzQ5MzE4Nzc1MWI1NzM0YTA4NTUxIiwicHJvZmlsZU5hbWUiOiJIYWthR2FtYmEiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZTY1MTlkYjI0MjFmZTY2MmFkNWFhZmM1YjljMmM0NmM2ZGM5NGIwNjdlODNjNjA0MWEzMTg1MzZiN2Q2ZCJ9fX0="),
   MUTATED_BANSHEE("MUTATED_BANSHEE", 25 , CC.translate("&f&k;&r&2&l☣&f&k;&r &2● &2&l&nMutated&r &a&lBanshee Pet &2● &f&k;&r&2&l☣&f&k;&r"), CC.translate("&f&k;&r&2&l☣&f&k;&r &2● &2&l&nMutated&r &a&lBanshee Pet &2● &f&k;&r&2&l☣&f&k;&r"), new InventoryMutatedBansheePet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNkOGQwYjBmZGQxMzgzNzY5Y2ZkOGIwYjFmYzA0NmQwNDBiYWVhMDAzNDEyNDY4N2JiYjFjNDI4Yzc3ODg2In19fQ====",
           "eyJ0aW1lc3RhbXAiOjE1OTQ2OTg0NjYxMzYsInByb2ZpbGVJZCI6IjNmNjUzNTNiODlhMzQ5MzE4Nzc1MWI1NzM0YTA4NTUxIiwicHJvZmlsZU5hbWUiOiJIYWthR2FtYmEiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZTY1MTlkYjI0MjFmZTY2MmFkNWFhZmM1YjljMmM0NmM2ZGM5NGIwNjdlODNjNjA0MWEzMTg1MzZiN2Q2ZCJ9fX0="),

   ABOMINABLE_SNOWMANPET("ABOMINABLE_SNOWMANPET", 25, CC.YellowB + "Abominable Snowman Pet", CC.YellowB + "Abominable Snowman", new InventoryAbominableSnowmanPet(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBhZTkxODQxYWZkMjQzZWMxOTgyYTc5ZmM0NTgyM2I0ZDM3NGY2MjFlMGQ3MzQ1MzNmODZkNzI1YTZmMGE0ZiJ9fX0="),
   XP_BOOSTER("EXP_BOOSTER",26,CC.DarkRedB + "XP Booster Pet", CC.DarkRedB + "XP Booster", new InventoryPetXpBooster(), "eyJ0aW1lc3RhbXAiOjE1NjA5NzQ0ODY1MDIsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84NzNkNTU4MzUyMmZkYTU3ZGQ1MzMwZjY1NmZjZjM2MjQ0Y2YwNDBkNjA0ODM0YTkxNzkwMTlkNjhhMzVhZmIxIn19fQ=="),
   ANTI_TELEBLOCK("ANTI_TELEBLOCK",27,CC.DarkRedB + "Anti Teleblock Pet", CC.DarkRedB + "Anti Teleblock Pet", new InventoryAntiTeleblockPet(), "eyJ0aW1lc3RhbXAiOjE1NjAyMTE0NDQ0NTksInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYjA5YTM3NTI1MTBlOTE0YjBiZGM5MDk2YjM5MmJiMzU5ZjdhOGU4YTk1NjZhMDJlN2Y2NmZhZmY4ZDZmODllIn19fQ=="),
   ;
   private final String itemName;
   private final String petName;
   private final InventoryPet internalClass;
   private final String skinBlob;
   private final String redSkinBlob;
   private final transient NBTTagCompoundBuilder builder;

   InventoryPets(final String s, final int n, final String itemName, final String petName, final InventoryPet instance, final String blob, final String redSkinBlob) {
      this.itemName = itemName;
      this.petName = petName;
      this.internalClass = instance;
      this.skinBlob = blob;
      this.redSkinBlob = redSkinBlob;
      GameProfile profile = new GameProfile(UUID.randomUUID(), this.name().toLowerCase());
      profile.getProperties().put("textures", new Property("textures", this.skinBlob));
      this.builder = new NBTTagCompoundBuilder();
      GameProfileSerializer.serialize(this.builder.get(), profile);
   }

   InventoryPets(final String s, final int n, final String itemName, final String petName, final InventoryPet instance, final String blob) {
      this.itemName = itemName;
      this.petName = petName;
      this.internalClass = instance;
      this.skinBlob = blob;
      this.redSkinBlob = null;
      GameProfile profile = new GameProfile(UUID.randomUUID(), this.name().toLowerCase());
      profile.getProperties().put("textures", new Property("textures", this.skinBlob));
      this.builder = new NBTTagCompoundBuilder();
      GameProfileSerializer.serialize(this.builder.get(), profile);
   }

   public String getMsgWithPrefix() {
      return this.getMsgWithPrefix(CC.Red);
   }

   public String getMsgWithPrefix(final String color) {
      return color + CC.Bold + "PET (" + color + ChatColor.stripColor(this.getPetName()) + color + CC.Bold + ")" + color + ":" + CC.White;
   }

   public void sendMsg(final Player player, final String color, final String msg) {
      player.sendMessage(this.getMsgWithPrefix(color) + " " + msg);
   }

   public void sendMsg(final Player player, final String msg) {
      this.sendMsg(player, CC.Red, msg);
   }

   public static InventoryPets fromName(final String name) {
      return Arrays.stream(values()).filter(f -> f.name().equalsIgnoreCase(name)).findFirst().orElse(null);
   }

   public String getRedSkinBlob() {
      return this.redSkinBlob;
   }

   public String getItemName() {
      return this.itemName;
   }

   public String getPetName() {
      return this.petName;
   }

   public InventoryPet getInternalClass() {
      return this.internalClass;
   }

   public String getSkinBlob() {
      return this.skinBlob;
   }

   public NBTTagCompoundBuilder getBuilder() {
      return this.builder;
   }
}
