package com.example.achess.nav;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */

public class Constant {

    Map<String, List<String>> menumap = new HashMap<String, List<String>>();

    public Constant() {
        this.menumap.put("Об игре", Arrays.asList  ("Правила"));
        this.menumap.put("Управление", Arrays.asList("Выход","Начать заново"));

    }


}
