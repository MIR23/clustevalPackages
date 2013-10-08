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
public class GMLDataSetFormatParser extends DataSetFormatParser {

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
		DataSet gml = dataSets.get(0);
		File inputPath = new File(gml.getAbsolutePath());

		new TextFileParser(inputPath.getAbsolutePath(), new int[0], new int[0],
				false) {

			protected boolean edgeParsing;
			protected String edgeSource, edgeTarget;
			protected boolean nodeParsing;
			protected String nodeId, nodeLabel;
			Map<String, String> nodeIdToLabel = new HashMap<String, String>();

			@Override
			protected void processLine(String[] key, String[] value) {
				String completeLine = value[0];
				if (completeLine.contains("graph ["))
					return;
				else if (completeLine.contains("node [")) {
					nodeParsing = true;
				} else if (nodeParsing && completeLine.contains("id")) {
					nodeId = completeLine
							.substring(completeLine.indexOf("id") + 3);
				} else if (nodeParsing && completeLine.contains("label")) {
					nodeLabel = completeLine.substring(
							completeLine.indexOf("label") + 6)
							.replace("\"", "");
				} else if (nodeParsing && completeLine.contains("index")) {
					if (!result.containsVertex(nodeLabel))
						result.addVertex(nodeLabel);
					nodeIdToLabel.put(nodeId, nodeLabel);
				} else if (completeLine.contains("edge [")) {
					edgeParsing = true;
				} else if (edgeParsing && completeLine.contains("source")) {
					edgeSource = completeLine.substring(completeLine
							.indexOf("source") + 7);
				} else if (edgeParsing && completeLine.contains("target")) {
					edgeTarget = completeLine.substring(completeLine
							.indexOf("target") + 7);

					result.addEdge(result.getEdgeCount() + "_" + edgeSource
							+ "_" + edgeTarget, nodeIdToLabel.get(edgeSource),
							nodeIdToLabel.get(edgeTarget));
				}
			}
		}.process();

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
