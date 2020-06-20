package it.polito.tdp.artsmia.model;

public class Arco implements Comparable<Arco> {

	private Artist vertice1;
	private Artist vertice2;
	private int peso;
	
	public Arco(Artist vertice1, Artist vertice2, int peso) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}

	public Artist getVertice1() {
		return vertice1;
	}

	public void setVertice1(Artist vertice1) {
		this.vertice1 = vertice1;
	}

	public Artist getVertice2() {
		return vertice2;
	}

	public void setVertice2(Artist vertice2) {
		this.vertice2 = vertice2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Arco o) {
		return o.peso-this.peso;
	}

	@Override
	public String toString() {
		return vertice1 + " - " + vertice2 + ": " + peso;
	}
	
	
	
}
