package me.axlerogue.weboflies.client;

import me.axlerogue.weboflies.network.ModMessages;
import me.axlerogue.weboflies.world.HomewardCobwebSavedData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class HomewardCobwebScreen extends Screen {
    private List<HomewardCobwebSavedData.CobwebEntry> cobwebs;

    public HomewardCobwebScreen(List<HomewardCobwebSavedData.CobwebEntry> cobwebs) {
        super(Component.literal("Homeward Cobweb Navigation"));
        this.cobwebs = cobwebs;
    }

    @Override
    protected void init() {
        super.init();
        updateButtons(this.cobwebs);
    }

    public void updateButtons(List<HomewardCobwebSavedData.CobwebEntry> cobwebs) {
        this.cobwebs = cobwebs;
        this.clearWidgets();
        
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 5;
        int startY = 40;

        for (int i = 0; i < cobwebs.size(); i++) {
            HomewardCobwebSavedData.CobwebEntry entry = cobwebs.get(i);
            this.addRenderableWidget(Button.builder(Component.literal(entry.name()), button -> {
                ModMessages.sendToServer(new ModMessages.ServerboundTeleportPacket(entry.pos(), entry.dimension()));
                this.onClose();
            }).bounds(this.width / 2 - buttonWidth / 2, startY + (buttonHeight + spacing) * i, buttonWidth, buttonHeight).build());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
