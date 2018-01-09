package com.example.achess;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Анюта on 22.08.2017.
 */

/**
 * Интерфейс Действие
 * в данной программе Действие может быть либо Сьесть фигуру либо Передвинуть
 * впринципе особо не нужен здесь интерфейс, просто захотелось попробовать
 */
public interface Action {


    ArrayList<Point> get_move_place(DBhelper dbHelp ,int x, int y, int fig_id );

}
