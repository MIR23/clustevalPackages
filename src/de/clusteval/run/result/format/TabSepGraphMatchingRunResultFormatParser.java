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
import java.util.Map;

import de.clusteval.data.DataConfig;

/**
 * @author Christian Wiwie
 * 
 */
public class TabSepGraphMatchingRunResultFormatParser
		extends
			RunResultFormatParser {

	/**
	 * @param internalParams
	 * @param params
	 * @param absFilePath
	 * @param dataConfig
	 * @throws IOException
	 */
	public TabSepGraphMatchingRunResultFormatParser(
			final Map<String, String> internalParams,
			final Map<String, String> params, final String absFilePath,
			final DataConfig dataConfig) throws IOException {
		super(internalParams, params, absFilePath, dataConfig);
	}

	@Override
	protected void processLine(@SuppressWarnings("unused") String[] key,
			@SuppressWarnings("unused") String[] value) {
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
