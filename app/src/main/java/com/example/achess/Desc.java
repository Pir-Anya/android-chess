package com.example.achess;


import android.content.res.Resources;
import android.graphics.*;

//import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;



import android.database.Cursor;
import android.support.v4.content.ContextCompat;

//import javax.swing.*;

/**
 Класс шахм Доска
 @author Romantsova
 */
public class Desc {
	/**
	 * @param w -ширина клетки на доске
	 * @param h -длина клетки на доске
	 */
	public Desc(int w, int h, Context cont) throws IOException {

		dbHelp = new DBhelper(cont);
		//dbHelp.opendatabase();
		Get_Figures();
		hod = Get_Hod();
		cell_width = w;
		cell_height = h;
		context = cont;
		//bitm = bitmap;
	}

	/**
	 загружает массив фигур из таблицы chess_desc  получае5т фигуры которые рассьтавлены на доске
	 */
	private void Get_Figures() {
		Figures.clear();
		dbHelp.opendatabase();
		Cursor rs = dbHelp.myDataBase.rawQuery("select * from chess_desc", null);
		int i = 0;
		while (rs.moveToNext()) {
			try {
				int x = rs.getInt(rs.getColumnIndex("x"));
				int y = rs.getInt(rs.getColumnIndex("y"));
				Figures.add( new Figure (x,y,dbHelp));
				i ++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		rs.close();
		dbHelp.close();
	}
	/**
	 * @return 0 - если ходят белые, 1 - если черные
	 */
	private int Get_Hod() {

		try {
			dbHelp.opendatabase();
			Cursor rs = dbHelp.myDataBase.rawQuery("Select * from hod",null);
			rs.moveToNext();
			int hod=rs.getInt(rs.getColumnIndex("hod"));
			rs.close();
			dbHelp.close();
			return  hod;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}
	/**
	 * рисует пустую доску
	 */
	public  void  Draw_desc(Paint paint, Canvas canvas){

		// Graphics2D g2 = (Graphics2D) g;
		int leftX = 0;
		int topY = 0;
		// Rectangle2D square;
		paint.setStyle(Paint.Style.FILL);
		for (int i=1; i<=8; i++) {
			for (int j=1; j<=8; j++) {

				if (j == 1) leftX = -cell_width;
				if ((i-j)%2 == 0 )
					 paint.setColor( ContextCompat.getColor(context,(R.color.colorGray)));
				 else
				    paint.setColor(ContextCompat.getColor(context,(R.color.colorWhite)));

				leftX = leftX + cell_width;
				// square = new Rectangle2D.Double(leftX, topY, cell_width, cell_height);
				canvas.drawRect(leftX, topY, cell_width+leftX, cell_height+topY, paint);
				// g2.fill(square);

			}
			topY   = topY + cell_height;
		}
		/*

*/

	}

	public String get_game_status(){
		String status_hod;
		int cur_hod = Get_Hod();
		if (!is_game_over()) {
			if (cur_hod==0) status_hod = "Ходят белые"; else status_hod = "Ходят черные";
			//canvas.drawText(status_hod, 3*cell_width, cell_height*8+50, paint);
		} else {
			if (cur_hod==1) status_hod = "Победили белые!!!"; else status_hod = "Победили черные!!!";
			//canvas.drawText(status_hod, 3*cell_width, cell_height*8+50, paint);
		}
		return status_hod;
	}

	private float dpFromPx(float px) {
		return px/context.getResources().getDisplayMetrics().density;
	}

	/**
	 * рисуем фигуры
	 * @param paint
	 * @param canvas
	 */

	public void Draw_Figures(Paint paint, Canvas canvas) {
		try {

			int x1;
			int y1;
			Bitmap bitmap;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.outHeight = cell_height;
			options.outWidth = cell_width;
			for(int i = 0; i < Figures.size(); i++)
			{
				Resources res = context.getResources();
				int figure_width = res.getInteger(R.integer.figure_height);
				x1 = (int) (Figures.get(i).x *cell_width-(cell_width-dpFromPx(figure_width)/2));
				y1 = (int) (8*cell_height-(Figures.get(i).y)*cell_height);
				InputStream ims = context.getAssets().open(Figures.get(i).patch);
				bitmap  = BitmapFactory.decodeStream(ims, null, options);
				canvas.drawBitmap(bitmap, x1, y1, paint);

				if (cur_selected_figure_id == Figures.get(i).figure_on_desc_id) {
					Rect r = new Rect((Figures.get(i).x-1)*cell_width , 8*cell_height -Figures.get(i).y*cell_height , (Figures.get(i).x-1)*cell_width+ cell_width, 8*cell_height -Figures.get(i).y*cell_height + cell_height);

				    // border
					paint.setStrokeWidth(2);
					paint.setStyle(Paint.Style.STROKE);
					paint.setColor(ContextCompat.getColor(context,(R.color.colorFreeCellsBorder)));
					canvas.drawRect(r, paint);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * рисует место куда можно поставить выделенную фигуру из cur_Cells

	 */

	public void Draw_Place(Paint paint, Canvas canvas ){
		Bitmap bitmap;
		if (cur_Cells != null) {

			// Rectangle2D rect;
			for (Point p: cur_Cells) {

				Rect r = new Rect((p.x-1)*cell_width , 8*cell_height -p.y*cell_height , (p.x-1)*cell_width+ cell_width, 8*cell_height -p.y*cell_height + cell_height);

				paint.setStyle(Paint.Style.FILL);
				paint.setColor(ContextCompat.getColor(context,(R.color.colorFreeCells)));
				canvas.drawRect(r, paint);

				// border
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(ContextCompat.getColor(context,(R.color.colorFreeCellsBorder)));
				canvas.drawRect(r, paint);

				// rect = new Rectangle2D.Double((p.x-1)*cell_width,8*cell_height -p.y*cell_height,cell_width, cell_height);
			}
		}

	}

	/**
	 * смена хода
	 */
	private void Change_Hod() {
		try {
			dbHelp.opendatabase();
			Cursor rs = dbHelp.myDataBase.rawQuery("Select * from hod",null);
			rs.moveToNext();
			int hod=rs.getInt(rs.getColumnIndex("hod"));
			if (hod==1)  {
				dbHelp.myDataBase.execSQL("update hod set hod = 0");
				this.hod = 0;
			} else {
				dbHelp.myDataBase.execSQL("update hod set hod = 1");
				this.hod = 1;
			}
			rs.close();
			dbHelp.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 *
	 * @return true - если конец игры
	 */
	public boolean is_game_over() {
		try {
			dbHelp.opendatabase();
			Cursor rs = dbHelp.myDataBase.rawQuery("Select count(*) as cnt from chess_desc where id_figure in (12,6)",null);
			rs.moveToNext();
			int cnt = rs.getInt(rs.getColumnIndex("cnt"));
			rs.close();
			dbHelp.close();
			if (cnt<2) return true; else return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}

	}

	/**
	 * возвращает массив клеток куда можно поставить фигуру
	 * @param x -координата x клетки где стоит фигура
	 * @param y - координата y клетки где стоит фигура
	 * @param fig_id - id фигуры на доске из таблицы chess_desc
	 * @return массив нужных клеток
	 */
	public  ArrayList<Point> get_cells(int x,int y,int fig_id) {

		Action act = new Eat();
        ArrayList<Point> cells1 = act.get_move_place(dbHelp,x,y,fig_id);
		act = new Move();
        ArrayList<Point> cells2 = act.get_move_place(dbHelp,x,y,fig_id);

		if (cells1 == null)	return cells2;
		if (cells2 == null)	return cells1;

        cells1.addAll(cells2);
        HashSet<Point> cells3 = new HashSet(cells1);
        ArrayList<Point> list = new ArrayList<Point>(cells3);
        return list;
	}





	/**
	 * по x, y ищет клетку, затем фигуру в этой клетке если она есть
	 * @param x - координаты клетки
	 * @param y - координаты клетки
	 * @return id фигуры на доске или 0 если клетка пуста
	 */
	public int get_figure_on_desc(int x, int y)
	{

		int id_figure = 0;
		if (x <= 8 && x>=1 && y >=1 && y <=8) {

			try {
				dbHelp.opendatabase();
				Cursor rs = dbHelp.myDataBase.rawQuery("Select * from chess_desc where x = "+x +" and y = "+y,null);
				if (rs.moveToNext()) {
					id_figure = rs.getInt(rs.getColumnIndex("id"));
					rs.close();
					dbHelp.close();
					return id_figure;
				}
				rs.close();
				dbHelp.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return id_figure;
	}
	/**
	 * @return true если клетка помещается на доске
	 */
	public static  boolean is_in_desc(int x,int y){
		if (x>8 || x<1 || y>8 ||y <1) return false;
		else return true;
	}

	/**
	 * @param x
	 * @param y
	 * @param fig_list -массив клеток
	 * @return true если (x,y) находится среди fig_list
	 */

	private boolean if_coursor_in_move_place(int x,int y, ArrayList<Point> fig_list) {
		if (fig_list == null) return false;
		for (Point p: fig_list) {
			if (p.x == x &&p.y ==y) return true;
		}
		return false;
	}

	/**
	 * очистка доски, расстановка фигур заново
	 */
	public  void clear_desc(){
		try {
			dbHelp.opendatabase();
			//dbHelper.opendatabase();
			dbHelp.myDataBase.execSQL("delete from chess_desc");
			dbHelp.myDataBase.execSQL("insert into chess_desc select * from clear_chess_desc");
			dbHelp.myDataBase.execSQL("update hod set hod=0");
			//	dbHelp.close();
            dbHelp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * совершение хода
	 * @param mouse_x -коорд мыши
	 * @param mouse_y -коорд мыши
	 */

	public void Play(int mouse_x,int mouse_y) {
		//Connection = getConnection();
		if (is_game_over()) return;
		int new_x = (int) Math.ceil((double) mouse_x / cell_width); //  Math.ceil((double)a / b).intValue();
		int new_y = (int) Math.ceil((double) (8 * cell_height - mouse_y) / cell_height);

		new_selected_figure_id = get_figure_on_desc(new_x, new_y);
		Figure new_figure = new Figure(new_selected_figure_id, dbHelp);
		Figure cur_figure = new Figure(cur_selected_figure_id, dbHelp);

		//если фигура было до этого выбрана и не ее ход
		hod = Get_Hod();
		if (cur_figure.Color >= 0 && hod != cur_figure.Color) return;
		if (cur_figure.Color <0 && new_figure.Color >= 0 && hod != new_figure.Color) return;

		if (new_selected_figure_id >0 && (new_figure.Color != cur_figure.Color))  new_Cells = get_cells(new_x, new_y, new_selected_figure_id);


		// выбрана пустая клетка
		if (new_selected_figure_id == 0) {
			//если была до этого выбрана фигура
			if (if_coursor_in_move_place(new_x, new_y, cur_Cells) ) {
				cur_figure.Put_to_new_place(new_x, new_y, hod);
				Change_Hod();
			}
			cur_selected_figure_id = new_selected_figure_id;
			cur_Cells = null;
		}

		//выбрана фигура
		if (new_selected_figure_id > 0) {
			//до этого не было выбрано фигуры
			if (cur_selected_figure_id == 0) {
				cur_selected_figure_id = new_selected_figure_id;
				cur_Cells = new_Cells;
			//	Change_Hod();
				return;
			}

			//если выбрана фигура другого цвета
			if (new_figure.Color != cur_figure.Color) {
				if (if_coursor_in_move_place(new_x, new_y, cur_Cells)) {
					cur_figure.Put_to_new_place(new_x, new_y, hod);
					Change_Hod();
					cur_selected_figure_id = 0;
					cur_Cells = null;
				}
			} else {
				cur_selected_figure_id = new_selected_figure_id;
				new_Cells = get_cells(new_x, new_y, new_selected_figure_id);
				cur_Cells = new_Cells;
			}
		}

		Get_Figures();
	}
		/*
		Figure cur_figure = new Figure(cur_selected_figure_id,dbHelp);
		Figure new_figure = new Figure(new_selected_figure_id,dbHelp);
		if (new_figure.Color == hod)
			new_Cells = show_move_place(new_x,new_y,new_selected_figure_id);
		else {
			if (new_figure.id>0) new_selected_figure_id = cur_selected_figure_id;
			new_Cells = null;
		}
		if (cur_figure.Color == hod) {

			if (cur_figure.Color != new_figure.Color && new_selected_figure_id>0) flag=true; else flag=false;
			if (cur_selected_figure_id >0 && (new_selected_figure_id ==0 || flag)) {
				if (if_coursor_in_move_place(new_x,new_y,cur_Cells) == true ) {
					cur_figure.Put_to_new_place(new_x,new_y,hod);
					Change_Hod();
					cur_Cells = null;
				};
			};
		};
		//if (new_figure.Color != cur_figure.Color )
		cur_selected_figure_id = new_selected_figure_id;
		if (new_Cells != null)
			cur_Cells = new ArrayList<Point>(new_Cells);
		Get_Figures();
		}
*/


	/**
	 * текущий ход
	 */
	public int hod;
	//private ResultSet rs;
	//private Connection conn;
	Context context;
	//private Statement statement;
	//private Bitmap bitm;
	private ArrayList<Figure> Figures = new ArrayList<Figure>();
	//private static ArrayList<Point> cells;
	private int cur_selected_figure_id = 0;
	private int new_selected_figure_id = 0;
	DBhelper dbHelp;
	private int cell_width;
	private int cell_height;
	ArrayList<Point> cur_Cells;
	ArrayList<Point> new_Cells;


}
