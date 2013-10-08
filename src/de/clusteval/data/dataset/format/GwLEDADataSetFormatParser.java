/*******************************************************************************
 * Copyright (c) 2013 Christian Wiwie.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Christian Wiwie - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package de.clusteval.data.dataset.format;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.parse.TextFileParser;
import de.clusteval.data.dataset.DataSet;
import de.clusteval.utils.FormatVersion;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * @author Christian Wiwie
 * 
 */
@FormatVersion(version = 1)
public class GwLEDADataSetFormatParser extends DataSetFormatParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.data.dataset.format.DataSetFormatParser#parse(de.clusteval
	 * .data.dataset.DataSet)
	 */
	@Override
	public DirectedSparseMultigraph<String, String> parse(List<DataSet> dataSets)
			throws IOException, InvalidDataSetFormatVersionException {
		final DirectedSparseMultigraph<String, String> result = new DirectedSparseMultigraph<String, String>();
		// the first one is the edgelist dataset
		DataSet edgeList = dataSets.get(0);
		File inputPath = new File(edgeList.getAbsolutePath());

		TextFileParser p = new TextFileParser(inputPath.getAbsolutePath(),
				new int[0], new int[0], true, " ") {

			protected int numberOfNodes;
			protected int numberOfEdges;
			protected Map<String, String> nodeIds = new HashMap<String, String>();

			@Override
			protected void processLine(String[] key, String[] value) {
				if (currentLine == 0 || currentLine == 1 || currentLine == 2)
					return;
				else if (currentLine == 3) {
					numberOfNodes = Integer.valueOf(value[0]);
				} else if (currentLine <= numberOfNodes + 3) {
					// parse a node
					String v = value[0];
					v = v.replace("|{", "");
					v = v.replace("}|", "");
					nodeIds.put((nodeIds.size() + 1) + "", v);
				} else if (currentLine == numberOfNodes + 4) {
					numberOfEdges = Integer.valueOf(value[0]);
				} else {
					// parse edge
					String s = value[0];
					String t = value[1];
					String e = value[3];
					e = e.replace("|{", "");
					e = e.replace("}|", "");
					result.addEdge(e + "_" + result.getEdgeCount(),
							nodeIds.get(s), nodeIds.get(t));
				}
			}
		};
		p.process();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.data.dataset.format.DataSetFormatParser#writeToFileHelper
	 * (de.clusteval.data.dataset.DataSet, java.io.BufferedWriter)
	 */
	@Override
	protected void writeToFileHelper(DataSet dataSet, BufferedWriter writer)
			throws IOException {
	}

}
