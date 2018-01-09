package com.example.achess;

import android.database.Cursor;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Анюта on 22.08.2017.
 */
public class Move implements Action{

    /**
     * возвращает массив клеток куда можно поставить фигуру
     * @param x -координата x клетки где стоит фигура
     * @param y - координата y клетки где стоит фигура
     * @param fig_id - id фигуры на доске из таблицы chess_desc
     * @return массив нужных клеток
     */
    @Override
    public ArrayList<Point> get_move_place(DBhelper dbHelp, int x, int y, int fig_id) {
        int move_x;
        int move_y;
        int sort;
        int movement_id;
        int id_figure;
        ArrayList<Point> cells = null;
        Point p =null;
        if (fig_id<=0) return null;
        try {
            Figure figure = new Figure(fig_id,dbHelp);
            dbHelp.opendatabase();
            Cursor rs = dbHelp.myDataBase.rawQuery("Select * from move where id_figure = "+figure.id,null);
            while (rs.moveToNext()) {
                move_x = rs.getInt(rs.getColumnIndex("move_x"));
                move_y = rs.getInt(rs.getColumnIndex("move_y"));
                sort = rs.getInt(rs.getColumnIndex("sort"));
                movement_id = rs.getInt(rs.getColumnIndex("movement_id"));
                if (Desc.is_in_desc(x+move_x, y+move_y)) {
                    if (if_free_cell(dbHelp,x,y,x+move_x,y+move_y,sort,movement_id,figure.id) == true) {
                        if (cells == null) cells = new ArrayList<Point>();
                        p = new Point();
                        p.x = x+move_x;
                        p.y = y+move_y;
                        cells.add(p);
                    };
                }
            }
            rs.close();
            dbHelp.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cells;
    }

    /**
     * проверка клетки (x1,y1) на то можно ли поставить на нее фигуру id_figure
     * @param x - координаты фигуры откуда надо переставить
     * @param y - координаты фигуры откуда надо переставить
     * @param x1 - координаты фигуры куда надо переставить
     * @param y1 - координаты фигуры куда надо переставить
     * @param sort - сортировка внутри движения movement_id
     * @param movement_id - id движения movement_id
     * @param id_figure
     * @param sort сортировка клетки
     * @return true если на пути до клетки(x1,y1) не было других фигур и клетка(x1,y1) пуста или на ней стоит чужая фигура
     */

    public boolean if_free_cell(DBhelper dbHelp, int x, int y, int x1, int y1, int sort, int movement_id, int id_figure) {
        try {
            int new_x;
            int new_y;
            Figure fig;
            Figure fig1;

            fig = new Figure(id_figure,dbHelp);
            dbHelp.opendatabase();


            //проверка клеток по пути до точки
            Cursor rs =  dbHelp.myDataBase.rawQuery("Select * from move where id_figure = "+id_figure+" and movement_id = "+movement_id+" and sort<= "+sort+" order by sort ",null);
            while  (rs.moveToNext()) {
                new_x = x+rs.getInt(rs.getColumnIndex("move_x"));
                new_y = y+rs.getInt(rs.getColumnIndex("move_y"));
                if (Desc.is_in_desc(new_x,new_y)) {
                    Cursor rs1 = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+new_x+" and y = "+new_y,null);
                    if (rs1.moveToNext()){
                        rs.close();
                        dbHelp.close();
                        return false;
                    }
                    rs1.close();
                }
            }
            rs.close();
            dbHelp.close();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
