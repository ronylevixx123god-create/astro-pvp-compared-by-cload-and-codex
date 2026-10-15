package com.astropvp.cc.packet;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PacketContainer implements Serializable {
   public static Gson GSON = (new GsonBuilder()).disableHtmlEscaping().setVersion(1.3D).registerTypeHierarchyAdapter(byte[].class, new PacketSerializer()).create();

   @SerializedName("protocol")
   private short protocol = 2;
   @SerializedName("packetID")
   private short packetID;
   @SerializedName("splitNext")
   private boolean splitNext;
   @SerializedName("content")
   private String content;

   public short getProtocol() {
      return this.protocol;
   }

   public short getPacketID() {
      return this.packetID;
   }

   public boolean isSplitNext() {
      return this.splitNext;
   }

   public String getContent() {
      return this.content;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PacketContainer)) {
         return false;
      } else {
         PacketContainer var2 = (PacketContainer)var1;
         if (!var2.a(this)) {
            return false;
         } else if (this.getProtocol() != var2.getProtocol()) {
            return false;
         } else if (this.getPacketID() != var2.getPacketID()) {
            return false;
         } else if (this.isSplitNext() != var2.isSplitNext()) {
            return false;
         } else {
            String var3 = this.getContent();
            String var4 = var2.getContent();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean a(Object var1) {
      return var1 instanceof PacketContainer;
   }

   public int hashCode() {
      byte var2 = 1;
      int var4 = var2 * 59 + this.getPacketID();
      var4 = var4 * 59 + this.getProtocol();
      var4 = var4 * 59 + (this.isSplitNext() ? 79 : 97);
      String var3 = this.getContent();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   public String toString() {
      return "CompiledMessage(protocol=" + this.getProtocol() + ", packetID=" + this.getPacketID() + ", splitNext=" + this.isSplitNext() + ", content=" + this.getContent() + ")";
   }

   public void setSplitNext(boolean splitNext) {
      this.splitNext = splitNext;
   }

   public PacketContainer(short var2, boolean var3, String var4) {
      this.packetID = var2;
      this.splitNext = var3;
      this.content = var4;
   }

   public PacketContainer(boolean var3, Packet var4) {
      this.packetID = var4.getID();
      this.splitNext = var3;
      this.content = var4.toJSON();
   }
}
