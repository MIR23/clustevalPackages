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
import java.util.List;

import utils.parse.TextFileParser;
import de.clusteval.data.dataset.DataSet;
import de.clusteval.framework.repository.RegisterException;
import de.clusteval.utils.FormatVersion;
import de.clusteval.utils.RNotAvailableException;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * @author Christian Wiwie
 * 
 */
@FormatVersion(version = 1)
public class EdgeListDataSetFormatParser extends DataSetFormatParser {

	@ParserConversions(conversions = {@StringMapping(key = "EdgeListDataSetFormat", value = "GwLEDADataSetFormat")})
	public DataSet convertEdgeToGwLEDA(DataSet dataSet,
			DataSetFormat targetFormat) throws IOException,
			InvalidDataSetFormatVersionException, RegisterException,
			UnknownDataSetFormatException, RNotAvailableException {
		return null;
	}

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

		new TextFileParser(inputPath.getAbsolutePath(), new int[0], new int[0]) {

			@Override
			protected void processLine(String[] key, String[] value) {
				String v1 = value[0];
				String v2 = value[1];

				if (!result.containsVertex(v1))
					result.addVertex(v1);

				if (!result.containsVertex(v2))
					result.addVertex(v2);

				result.addEdge(result.getEdgeCount() + "", v1, v2);
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
