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
package de.clusteval.quality;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.clusteval.data.DataConfig;
import de.clusteval.data.dataset.format.InvalidDataSetFormatVersionException;
import de.clusteval.data.dataset.format.UnknownDataSetFormatException;
import de.clusteval.data.goldstandard.format.UnknownGoldStandardFormatException;
import de.clusteval.framework.repository.RegisterException;
import de.clusteval.framework.repository.Repository;
import de.clusteval.graphmatching.GraphMatching;
import de.clusteval.utils.RCalculationException;
import de.clusteval.utils.RNotAvailableException;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * @author Christian Wiwie
 * 
 */
public class EdgeCorrectnessQualityMeasure extends QualityMeasure {

	/**
	 * @param repo
	 * @param register
	 * @param changeDate
	 * @param absPath
	 * @throws RegisterException
	 */
	public EdgeCorrectnessQualityMeasure(Repository repo, boolean register,
			long changeDate, File absPath) throws RegisterException {
		super(repo, register, changeDate, absPath);
	}

	/**
	 * @param other
	 * @throws RegisterException
	 */
	public EdgeCorrectnessQualityMeasure(EdgeCorrectnessQualityMeasure other)
			throws RegisterException {
		super(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.cluster.quality.ClusteringQualityMeasure#getQualityOfClustering
	 * (de.clusteval.cluster.Clustering, de.clusteval.cluster.Clustering,
	 * de.clusteval.data.DataConfig)
	 */
	@Override
	public QualityMeasureValue getQualityOf(GraphMatching matching,
			GraphMatching goldStandard, DataConfig dataConfig)
			throws UnknownGoldStandardFormatException,
			UnknownDataSetFormatException, IOException,
			InvalidDataSetFormatVersionException, RNotAvailableException,
			RCalculationException {
		// get the graphs
		List<DirectedSparseMultigraph<String, String>> graphs = dataConfig
				.getDatasetConfig().getGraphs();
		DirectedSparseMultigraph<String, String> g1 = graphs.get(0);
		DirectedSparseMultigraph<String, String> g2 = graphs.get(1);

		int ae = 0;

		for (String e : g1.getEdges()) {
			String s = g1.getSource(e);
			String t = g1.getDest(e);

			String sMapped = matching.getMatchingForGraph1Vertex(s);
			String tMapped = matching.getMatchingForGraph1Vertex(t);

			if (g2.findEdge(sMapped, tMapped) != null)
				ae++;
		}

		return QualityMeasureValue.getForDouble((double) ae
				/ Math.min(g1.getEdgeCount(), g2.getEdgeCount()) * 100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.cluster.quality.ClusteringQualityMeasure#isBetterThanHelper
	 * (de.clusteval.cluster.quality.ClusteringQualityMeasureValue,
	 * de.clusteval.cluster.quality.ClusteringQualityMeasureValue)
	 */
	@Override
	protected boolean isBetterThanHelper(QualityMeasureValue quality1,
			QualityMeasureValue quality2) {
		return quality1.getValue() > quality2.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.clusteval.cluster.quality.ClusteringQualityMeasure#getMinimum()
	 */
	@Override
	public double getMinimum() {
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.clusteval.cluster.quality.ClusteringQualityMeasure#getMaximum()
	 */
	@Override
	public double getMaximum() {
		return 100.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.cluster.quality.ClusteringQualityMeasure#getRequiredRlibraries
	 * ()
	 */
	@Override
	public Set<String> getRequiredRlibraries() {
		return new HashSet<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.clusteval.cluster.quality.ClusteringQualityMeasure#requiresGoldstandard
	 * ()
	 */
	@Override
	public boolean requiresGoldstandard() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.clusteval.cluster.quality.ClusteringQualityMeasure#getAlias()
	 */
	@Override
	public String getAlias() {
		return "Edge Correctness";
	}

}
