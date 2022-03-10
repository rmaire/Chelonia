/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

class GuiToggleAdapter extends InputAdapter {

    private boolean collapsed = false;
    private boolean editorVisible = false;
    private boolean changed = false;

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.F1) {
            collapsed = !collapsed;
            changed = true;
        }
        if (keycode == Input.Keys.F2) {
            editorVisible = !editorVisible;
            changed = true;
        }
        return false;
    }

    public int getReplHeight() {
        return collapsed ? 0 : 250;
    }

    public int getToolsBarWidth() {
        return collapsed ? 0 : 150;
    }

    public boolean isEditorVisible() {
        if(collapsed){
            return false;
        }
        return editorVisible;
    }
    
    public void setEditorVisible(boolean visible) {
        editorVisible = visible;
    }
    
    public boolean hasChanged() {
        if (changed) {
            changed = false;
            return true;
        } else {
            return false;
        }
    }
}
