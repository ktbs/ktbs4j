package org.liris.ktbs.java.tests;

import org.liris.ktbs.TraceBuilder;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsResourceWriter;

public class CoreTests {
	public static void main(String[] args) {
		TraceBuilder builder = new TraceBuilder();
		builder.createNewTrace("http://mydomain/mytrace/", "http://mydomain/mymodel/", "Ma super trace de Damien", "15-12-2010", "14:15:00");
		builder.addInstantObsel("14:18:00", "Envoyermail", "destinataire", "Damien", "message", "Salut tout le monde, regardez ces pièces jointes", "nbpiecesjointes", 3);
		builder.addInstantObsel("14:21:21", "Recevoirmail", "expediteur", "PA", "message", "Très intéressant");
		Obsel obs1 = builder.addInstantObsel("14:29:50", "Recevoirmail", "expediteur", "Yannick", "message", "J'ai modifié le rapport", "nbpiecesjointes", 1);
		Obsel obs2 = builder.addObsel("14:29:50", "14:50:00", "Document", "titre", "Rapport de réunion", "taille", 2678);
		builder.addRelation(obs1, obs2, "piecejointe");
		
		Trace trace = builder.getTrace();
		
		KtbsResourceWriter writer = new KtbsResourceWriter(trace);
		String s = writer.serializeToString(JenaConstants.JENA_SYNTAX_TURTLE,false);
		
		System.out.println(s);
	}
}
