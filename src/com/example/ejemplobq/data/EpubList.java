package com.example.ejemplobq.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EpubList {
	
	private List<EpubItem> listaEpub;
	
	public EpubList()
	{	
		this.listaEpub = new LinkedList<EpubItem>();
	}

	public List<EpubItem> getListaEpub() {
		return listaEpub;
	}

	public void setListaEpub(List<EpubItem> listaEpub) {
		this.listaEpub = listaEpub;
	}
	
	public void addItem(EpubItem epubItem)
	{		
		listaEpub.add(epubItem);
	}
	
	public void clearList()
	{
		listaEpub.clear();
	}
	
	public int size()
	{
		return this.listaEpub.size();
	}
	
	public EpubItem get(int position)
	{
		return this.listaEpub.get(position);
	}
	
	public List<EpubItem> orderByName()
	{
		boolean encontrado;
		String name;
		
		for(int i = 1; i < this.listaEpub.size();i++)
		{
			encontrado = false;
			name = ((EpubItem)listaEpub.get(i)).getName();
			for(int j=0; j < this.listaEpub.size() && !encontrado; j++)
			{
				if(name.compareToIgnoreCase(((EpubItem)listaEpub.get(j)).getName())  == 0)
				{					
					encontrado = true;
				}
				else if(name.compareToIgnoreCase(((EpubItem)listaEpub.get(j)).getName())  < 0)
				{
					this.listaEpub.add(j,(EpubItem)listaEpub.get(i));
					this.listaEpub.remove(i+1);
					encontrado = true;
				}
			}
		}
		return this.listaEpub;
	}
	
	public List<EpubItem> orderByDate()
	{
		boolean encontrado;
		Date date;
		
		for(int i = 1; i < this.listaEpub.size();i++)
		{
			encontrado = false;
			date = ((EpubItem)listaEpub.get(i)).getDate();
			for(int j=0; j < this.listaEpub.size() && !encontrado; j++)
			{
				if(date.compareTo(((EpubItem)listaEpub.get(j)).getDate())  == 0)
				{					
					encontrado = true;
				}
				else if(date.compareTo(((EpubItem)listaEpub.get(j)).getDate())  < 0)
				{
					this.listaEpub.add(j,(EpubItem)listaEpub.get(i));
					this.listaEpub.remove(i+1);
					encontrado = true;
				}
			}
		}
		return this.listaEpub;
	}
}