package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private Graph<Album,DefaultEdge> grafo;
	//per la ricorsione
	private int dimMax;
	private Set<Album> setMassimo;
	
	
	public void creaGrafo(double durata) {
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		
		ItunesDAO dao = new ItunesDAO();
		Graphs.addAllVertices(this.grafo, dao.getAlbumsWithDuration(durata));
		Map<Integer,Album> albumIdMap=new HashMap<>();
		for (Album a:this.grafo.vertexSet()) {
			albumIdMap.put(a.getAlbumId(), a);
		}
		
		//aggiungo i veritici
		
		List<Pair<Integer,Integer>> archi=dao.getCompatibleAlbums();
		for (Pair<Integer,Integer> arco: archi) {
			if(albumIdMap.containsKey(arco.getFirst()) && albumIdMap.containsKey(arco.getSecond()) && !arco.getFirst().equals(arco.getSecond())) {
				this.grafo.addEdge(albumIdMap.get(arco.getFirst()), albumIdMap.get(arco.getSecond()));
			}
		}
		System.out.println("Vertici: "+this.grafo.vertexSet().size());
		System.out.println("Archi: "+this.grafo.edgeSet().size());
	}
	
	public List<Album> getAlbums(){
		List<Album> list=new ArrayList<Album>(this.grafo.vertexSet());
		
		Collections.sort(list);
		return list;
		
	}
	
	public Set<Album> getComponente (Album al){
		ConnectivityInspector<Album,DefaultEdge>ci=new ConnectivityInspector(this.grafo);
		return ci.connectedSetOf(al);
		
	}
	
	public Set<Album> ricercaSetMassimo(Album a1,double dTot){
		if (a1.getDurata()>dTot) {
			return null;
		}
		List<Album> parziale=new ArrayList<>();
		List<Album> tutti=new ArrayList<>(getComponente(a1));
		dimMax=0;
		setMassimo=null;
		//parziale.add(a1);
		tutti.remove(a1);
		cerca(parziale,0,dTot-a1.getDurata(),tutti,0.0);
		Set<Album> result =new HashSet<>(this.setMassimo);
		result.add(a1);
		return this.setMassimo;
		
	}
	
	private void cerca(List<Album> parziale, int livello, double dTot, List<Album> tutti,double durataParziale) {
		//condizione di terminazione
		if(parziale.size()>dimMax) {
			dimMax=parziale.size();
			this.setMassimo=new HashSet<Album>(parziale);
			return;
		}
		
		for (Album a: tutti) {
			if((livello==0)||(a.getAlbumId()>parziale.get(parziale.size()-1).getAlbumId() && durataParziale+a.getDurata()<=dTot)) {			
				parziale.add(a);
				cerca(parziale,livello+1,dTot,tutti,durataParziale+a.getDurata());
				parziale.remove(parziale.size()-1);	
			}	
		}
		
		
		
	}
	
}
