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
package de.clusteval.run.result.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.clusteval.data.DataConfig;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * @author Christian Wiwie
 * 
 */
public class SpinalRunResultFormatParser extends RunResultFormatParser {

	protected List<DirectedSparseMultigraph<String, String>> graphs;

	protected List<Map<String, String>> nodeIdToLabel;

	/**
	 * @param internalParams
	 * @param params
	 * @param absFilePath
	 * @param dataConfig
	 * @throws IOException
	 */
	public SpinalRunResultFormatParser(
			final Map<String, String> internalParams,
			final Map<String, String> params, final String absFilePath,
			final DataConfig dataConfig) throws IOException {
		super(internalParams, params, absFilePath, dataConfig);
		this.graphs = dataConfig.getDatasetConfig().getGraphs();
		this.nodeIdToLabel = new ArrayList<Map<String, String>>();
		for (DirectedSparseMultigraph<String, String> graph : this.graphs) {
			Map<String, String> idToLabel = new HashMap<String, String>();
			for (String e : graph.getEdges()) {
				String[] split = e.split("_");
				if (!idToLabel.containsKey(split[1])) {
					idToLabel.put(split[1], graph.getSource(e));
				}
				if (!idToLabel.containsKey(split[2])) {
					idToLabel.put(split[2], graph.getDest(e));
				}
			}
			this.nodeIdToLabel.add(idToLabel);
		}
		this.skipEmptyLines();
		this.inSplit = " ";
		this.countLines();
	}

	@Override
	protected void processLine(@SuppressWarnings("unused") String[] key,
			@SuppressWarnings("unused") String[] value) {
	}

	@Override
	protected String getLineOutput(String[] key, String[] value) {
		StringBuilder sb = new StringBuilder();
		if (currentLine < 2)
			return null;
		sb.append(super.getLineOutput(key, value));
		sb.append(this.nodeIdToLabel.get(0).get(value[0]));
		sb.append(",");
		sb.append(this.nodeIdToLabel.get(1).get(value[1]));
		sb.append(";");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see run.result.format.RunResultFormatParser#convertToStandardFormat()
	 */
	@Override
	public void convertToStandardFormat() throws IOException {
		this.process();
	}
}
