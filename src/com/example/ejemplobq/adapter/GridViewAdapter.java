package com.example.ejemplobq.adapter;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejemplobq.R;
import com.example.ejemplobq.data.EpubItem;
import com.example.ejemplobq.data.EpubList;

public class GridViewAdapter extends BaseAdapter
{    
	public static final int ORDER_BY_NAME = 0;
	public static final int ORDER_BY_DATE = 1;
	
	/*private ArrayList<ImageView> listImage;
	private ArrayList<String> listName;
    private ArrayList<String> listDate;
    */
	private EpubList lista;    
    
    private Activity activity;
 
    public GridViewAdapter(Activity activity, EpubList lista) {
        super();
        this.lista = lista;      
        this.activity = activity;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lista.size();
    }
 
    @Override
    public EpubItem getItem(int position) {
        // TODO Auto-generated method stub
        return lista.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void orderList(int orderBy)
    {
    	if(orderBy == ORDER_BY_NAME)
    	{
    		this.lista.orderByName();
    	}
    	else if (orderBy == ORDER_BY_DATE)
    	{
    		this.lista.orderByDate();
    	}
    }
 
    public static class ViewHolder
    {
        public ImageView imgViewIcon;
        public TextView txtViewName;
        public TextView txtViewDate;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        SimpleDateFormat df;
        LayoutInflater inflator = activity.getLayoutInflater();
 
        if(convertView==null)
        {
            view = new ViewHolder();    
            
            convertView = inflator.inflate(R.layout.gridview_row, null);
 
            view.imgViewIcon = (ImageView) convertView.findViewById(R.id.imageGV);
            view.txtViewName = (TextView) convertView.findViewById(R.id.txtNombre);
            view.txtViewDate = (TextView) convertView.findViewById(R.id.txtFecha);
            
           
            view.imgViewIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub					
					Log.i("CLICK", "GRID IMAGE VIEW");					
				}
			});
 
            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }
        
       
        df = new SimpleDateFormat("dd/MM/yy");
 
        view.imgViewIcon.setImageResource(R.drawable.ic_launcher);
        view.txtViewName.setText(((EpubItem)lista.get(position)).getName());
        view.txtViewDate.setText(df.format((((EpubItem)lista.get(position)).getDate())));
        
 
        return convertView;
    }
    
    
}