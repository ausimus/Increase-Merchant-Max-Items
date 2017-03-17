package org.ausimus.wurmunlimited.mods.increasemerchantitems;
import javassist.*;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import java.util.Properties;

public class IncreaseMerchantItems implements WurmServerMod, Configurable, PreInitable {
    private int MaxItems;

    @Override
    public void preInit() {
        try {
            CtClass ctClassDT = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.TradeHandler");
            CtMethod checkDigTile = ctClassDT.getMethod("suckInterestingItems", Descriptor.ofMethod(CtPrimitiveType.voidType, null));
            checkDigTile.setBody("{\n" +
                    "        com.wurmonline.server.items.TradingWindow currWin = this.trade.getTradingWindow(2L);\n" +
                    "        com.wurmonline.server.items.TradingWindow targetWin = this.trade.getTradingWindow(4L);\n" +
                    "        com.wurmonline.server.items.Item[] offItems = currWin.getItems();\n" +
                    "        com.wurmonline.server.items.Item[] setItems = targetWin.getItems();\n" +
                    "        int x;\n" +
                    "        if(!this.shop.isPersonal()) {\n" +
                    "            this.currentDemandMap.clear();\n" +
                    "\n" +
                    "            int parent;\n" +
                    "            java.lang.Object size;\n" +
                    "            for(x = 0; x < setItems.length; ++x) {\n" +
                    "                parent = setItems[x].getTemplateId();\n" +
                    "                size = (java.util.List)this.currentDemandMap.get(Integer.valueOf(parent));\n" +
                    "                if(size == null) {\n" +
                    "                    size = new java.util.LinkedList();\n" +
                    "                }\n" +
                    "\n" +
                    "                ((java.util.List)size).add(setItems[x]);\n" +
                    "                this.currentDemandMap.put(Integer.valueOf(parent), size);\n" +
                    "            }\n" +
                    "\n" +
                    "            boolean var13 = true;\n" +
                    "            targetWin.startReceivingItems();\n" +
                    "\n" +
                    "            for(parent = 0; parent < offItems.length; ++parent) {\n" +
                    "                if(!offItems[parent].isArtifact() && offItems[parent].isPurchased() && offItems[parent].getLockId() == -10L) {\n" +
                    "                    com.wurmonline.server.items.Item var18 = offItems[parent];\n" +
                    "\n" +
                    "                    try {\n" +
                    "                        var18 = offItems[parent].getParent();\n" +
                    "                    } catch (com.wurmonline.server.NoSuchItemException var12) {\n" +
                    "                        ;\n" +
                    "                    }\n" +
                    "\n" +
                    "                    if(offItems[parent] == var18 || var18.isViewableBy(this.creature)) {\n" +
                    "                        if(offItems[parent].isHollow() && !offItems[parent].isEmpty(true)) {\n" +
                    "                            this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature.getName() + \" says, \\'Please empty the \" + offItems[parent].getName() + \" first.\\'\");\n" +
                    "                        } else {\n" +
                    "                            x = offItems[parent].getTemplateId();\n" +
                    "                            Object hisReq = (java.util.List)this.currentDemandMap.get(Integer.valueOf(x));\n" +
                    "                            if(hisReq == null) {\n" +
                    "                                hisReq = new java.util.LinkedList();\n" +
                    "                            }\n" +
                    "\n" +
                    "                            if((float)((java.util.List)hisReq).size() < 80.0F) {\n" +
                    "                                currWin.removeItem(offItems[parent]);\n" +
                    "                                targetWin.addItem(offItems[parent]);\n" +
                    "                                ((java.util.List)hisReq).add(offItems[parent]);\n" +
                    "                                this.currentDemandMap.put(Integer.valueOf(x), hisReq);\n" +
                    "                            }\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                } else if((offItems[parent].isHomesteadDeed() || offItems[parent].isVillageDeed()) && offItems[parent].getData2() <= 0) {\n" +
                    "                    x = offItems[parent].getTemplateId();\n" +
                    "                    size = (java.util.List)this.currentDemandMap.get(Integer.valueOf(x));\n" +
                    "                    if(size == null) {\n" +
                    "                        size = new java.util.LinkedList();\n" +
                    "                    }\n" +
                    "\n" +
                    "                    currWin.removeItem(offItems[parent]);\n" +
                    "                    targetWin.addItem(offItems[parent]);\n" +
                    "                    ((java.util.List)size).add(offItems[parent]);\n" +
                    "                    this.currentDemandMap.put(Integer.valueOf(x), size);\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            targetWin.stopReceivingItems();\n" +
                    "        } else if(this.ownerTrade) {\n" +
                    "            com.wurmonline.server.items.TradingWindow var14 = this.trade.getTradingWindow(1L);\n" +
                    "            com.wurmonline.server.items.Item[] var15 = var14.getItems();\n" +
                    "            int var20 = 0;\n" +
                    "\n" +
                    "            for(int var17 = 0; var17 < var15.length; ++var17) {\n" +
                    "                if(!var15[var17].isCoin()) {\n" +
                    "                    ++var20;\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            var20 += setItems.length;\n" +
                    "            if(var20 > " + MaxItems + ") {\n" +
                    "                this.trade.creatureOne.getCommunicator().sendNormalServerMessage(this.creature.getName() + \" says, \\'I cannot add more items to my stock right now.\\'\");\n" +
                    "            } else {\n" +
                    "                com.wurmonline.server.items.TradingWindow var19 = this.trade.getTradingWindow(3L);\n" +
                    "                com.wurmonline.server.items.Item[] reqItems = var19.getItems();\n" +
                    "\n" +
                    "                int x1;\n" +
                    "                for(x1 = 0; x1 < reqItems.length; ++x1) {\n" +
                    "                    if(!reqItems[x1].isCoin()) {\n" +
                    "                        ++var20;\n" +
                    "                    }\n" +
                    "                }\n" +
                    "\n" +
                    "                if(var20 > " + MaxItems + ") {\n" +
                    "                    this.trade.creatureOne.getCommunicator().sendNormalServerMessage(this.creature.getName() + \" says, \\'I cannot add more items to my stock right now.\\'\");\n" +
                    "                } else {\n" +
                    "                    targetWin.startReceivingItems();\n" +
                    "\n" +
                    "                    for(x1 = 0; x1 < offItems.length; ++x1) {\n" +
                    "                        if(offItems[x1].getTemplateId() != 272 && offItems[x1].getTemplateId() != 781 && !offItems[x1].isArtifact() && !offItems[x1].isRoyal() && (!offItems[x1].isVillageDeed() && !offItems[x1].isHomesteadDeed() || !offItems[x1].hasData())) {\n" +
                    "                            if(var20 > "+MaxItems+") {\n" +
                    "                                if(offItems[x1].isCoin()) {\n" +
                    "                                    currWin.removeItem(offItems[x1]);\n" +
                    "                                    targetWin.addItem(offItems[x1]);\n" +
                    "                                }\n" +
                    "                            } else if((!offItems[x1].isLockable() || !offItems[x1].isLocked()) && (!offItems[x1].isHollow() || offItems[x1].isEmpty(true))) {\n" +
                    "                                currWin.removeItem(offItems[x1]);\n" +
                    "                                targetWin.addItem(offItems[x1]);\n" +
                    "                                ++var20;\n" +
                    "                            } else if(offItems[x1].isLockable() && offItems[x1].isLocked()) {\n" +
                    "                                this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature.getName() + \" says, \\'I don\\'t accept locked items any more. Sorry for the inconvenience.\\'\");\n" +
                    "                            } else {\n" +
                    "                                this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature.getName() + \" says, \\'Please empty the \" + offItems[x1].getName() + \" first.\\'\");\n" +
                    "                            }\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "\n" +
                    "                    targetWin.stopReceivingItems();\n" +
                    "                }\n" +
                    "            }\n" +
                    "        } else {\n" +
                    "            targetWin.startReceivingItems();\n" +
                    "\n" +
                    "            for(x = 0; x < offItems.length; ++x) {\n" +
                    "                if(offItems[x].isCoin()) {\n" +
                    "                    com.wurmonline.server.items.Item var16 = offItems[x];\n" +
                    "\n" +
                    "                    try {\n" +
                    "                        var16 = offItems[x].getParent();\n" +
                    "                    } catch (com.wurmonline.server.NoSuchItemException var11) {\n" +
                    "                        ;\n" +
                    "                    }\n" +
                    "\n" +
                    "                    if(offItems[x] == var16 || var16.isViewableBy(this.creature)) {\n" +
                    "                        currWin.removeItem(offItems[x]);\n" +
                    "                        targetWin.addItem(offItems[x]);\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            targetWin.stopReceivingItems();\n" +
                    "        }\n" +
                    "\n" +
                    "    }");
        } catch (CannotCompileException | NotFoundException ex) {
            throw new HookException(ex);
        }
    }

    @Override
    public void configure(Properties properties) {
        MaxItems = Integer.parseInt(properties.getProperty("MaxItems", Integer.toString(MaxItems)));
    }
}