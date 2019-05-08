package br.com.welike.instagram.helper;

import java.util.List;

public class Utils {

    public static Object getLastElement(List<Object> list) {
        return list.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

}
