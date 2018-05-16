package com.txtled.gp_a012.menu.service;

import java.util.Observer;

/**
 * Created by KomoriWu on 2017/6/24.
 */

public interface ConnBleInterface {
   void scanBle();
   void addObserver(Observer observer);
}
