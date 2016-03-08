/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;

import GameOfLife.GameController;


/**
 *
 * @author Stian Reistad Rogeberg, Terje Leirvik, Robin S. A. D. Lundh
 */
public class ButtonListener {
    private GameController gc;
    
    public void play() {
        gc.play();
    }
}
