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
import java.io.IOException;
import java.util.List;

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
public class NodeSignatureDataSetFormatParser extends DataSetFormatParser {

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
		return null;
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
