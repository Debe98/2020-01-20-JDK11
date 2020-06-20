package it.polito.tdp.artsmia.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Map <Integer, Artist> artisti;
	private ArtsmiaDAO dao;
	private Graph <Artist, DefaultWeightedEdge> grafo;
	private List <Arco> archiUtili;
	List <Artist> best;

	public Model() {
		artisti = new HashMap <> ();
		dao = new ArtsmiaDAO();
		archiUtili = null;
	}
	
	public void creaGrafo(String ruolo) {
		grafo = new SimpleWeightedGraph<Artist, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao.listArtists(artisti);
		
		//Get Vertici
		Graphs.addAllVertices(grafo, dao.getVertex(artisti, ruolo));
		System.out.println("#Vertici: "+grafo.vertexSet().size());
		
		//Get Archi 
		List <Arco> archi = dao.getArchi(artisti, ruolo);
		archiUtili = archi;
		System.out.println("#Archi-pre: "+archi.size());
		
		for (Arco a : archi) {
			Artist v1 = a.getVertice1();
			Artist v2 = a.getVertice2();
			if (grafo.containsVertex(v1) && grafo.containsVertex(v2)) {
				if (!grafo.containsEdge(v1, v2)) {
					Graphs.addEdge(grafo, v1, v2, a.getPeso());
				}
			}
		}
		System.out.println("#Archi-post: "+grafo.edgeSet().size());
	}
	
	public List<Arco> getArchi() {
		if (grafo == null)
			return null;
		Collections.sort(archiUtili);
		return archiUtili;
	}

	public List <String> getRuoli() {
		return dao.getRuoli();
	}

	public List <Artist> getPercorso(int n) {
		Artist a = artisti.get(n);
		if (grafo == null || !grafo.containsVertex(a)) {
			return null;
		}
		
		List <Artist> vicini = Graphs.neighborListOf(grafo, a);
		Set <Integer> pesi = new HashSet<Integer>();
		
		for (Artist vicino: vicini) {
			DefaultWeightedEdge e = grafo.getEdge(a, vicino);
			pesi.add((int) grafo.getEdgeWeight(e));
		}
		
		System.out.println(pesi);
		
		for (Integer i : pesi) {
			List <Artist> lista = new LinkedList<Artist>();
			doRicorsione(a, lista, i);
		}
		
		return best;
		
	}
	
	
	public void doRicorsione (Artist sorgente, List<Artist> percorso, int peso) {
		
		List <Artist> vicini = Graphs.neighborListOf(grafo, sorgente);
		List <Artist> viciniCorretti = new LinkedList<Artist>();
		
		for (Artist a : vicini) {
			DefaultWeightedEdge e = grafo.getEdge(sorgente, a);
			if (grafo.getEdgeWeight(e) == peso) {
				viciniCorretti.add(a);
			}
		}
		
		viciniCorretti.removeAll(percorso);
		
		if (viciniCorretti.isEmpty()) {
			if (best == null || best.size() < percorso.size()) {
				best = new LinkedList<Artist> (percorso);
			}
			System.out.println(percorso);
			return;
		}
		
		for (Artist a : viciniCorretti) {
			DefaultWeightedEdge e = grafo.getEdge(sorgente, a);
			if (grafo.getEdgeWeight(e) == peso) {
				percorso.add(sorgente);
				doRicorsione(a, percorso, peso);
				percorso.remove(sorgente);
			}
		}
	}
	
	
}
