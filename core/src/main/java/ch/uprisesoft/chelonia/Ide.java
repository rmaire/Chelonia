/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

/**
 *
 * @author rma
 */
public interface Ide {
    public void toggleEditor(String content);
    public void toggleEditor();
    public void toggleRepl();
}
