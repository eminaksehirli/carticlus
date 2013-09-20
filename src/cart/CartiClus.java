/* User Interface for the CartiClus Algorithm. 
 *  
 * Copyright (C) 2011 - 2013  Emin Aksehirli
 *
 * This file is part of CartiClus - http://adrem.ua.ac.be/cartification .
 * 
 * MIME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cart;

import static com.google.common.io.ByteStreams.nullOutputStream;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Math.ceil;
import i9.subspace.base.Cluster;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

public class CartiClus
{
	public static final int N_Threads = (int) ceil(0.75 * Runtime.getRuntime()
			.availableProcessors());

	public static void main(String[] args) throws Exception
	{
		String configFileName;
		if (args.length < 1)
		{
			System.out.println("Using default file! Usage: java -jar carticlus.jar configFile.properties");
			configFileName = "default.properties";
		} else
		{
			configFileName = args[0];
		}

		final Properties p = new Properties();
		p.load(new FileReader(configFileName));

		final int expectedClusterSize = parseInt(p.getProperty("expectedClusterSize"));
		final int numOfDimensions = parseInt(p.getProperty("numOfDimensions"));
		final int expectedNumOfDims = parseInt(p.getProperty("expectedNumOfDims"));
		final double kRatio = parseDouble(p.getProperty("kRatio"));
		final double minSupRatio = parseDouble(p.getProperty("minSupRatio"));
		String dataFile = p.getProperty("dataFile");

		final String outFile = p.getProperty("cartLog");
		PrintStream outWriter = System.out;
		if (outFile != null && outFile.length() > 1)
		{
			outWriter = new PrintStream(new FileOutputStream(outFile));
		}

		final String cartLogFile = p.getProperty("cartLog");
		PrintWriter cartLog;
		if (cartLogFile != null && cartLogFile.length() > 1)
		{
			cartLog = new PrintWriter(new BufferedWriter(new FileWriter(cartLogFile)));
		} else
		{
			cartLog = new PrintWriter(nullOutputStream());
		}

		System.out.println("Running: " + " dataFile: " + dataFile
				+ ", expectedClusterSize: " + expectedClusterSize
				+ ", numOfDimensions: " + numOfDimensions + ", expectedNumOfDims: "
				+ expectedNumOfDims + ", kRatio:" + kRatio + ", minSupRatio: "
				+ minSupRatio);

		ClusterFinder cartRunner = new ClusterFinder(expectedClusterSize,
				numOfDimensions,
				expectedNumOfDims,
				cartLog);

		cartLog.println("====== " + dataFile + " ======");
		List<Cluster> cartClusters = cartRunner.run(dataFile, kRatio, minSupRatio);

		for (Cluster cluster : cartClusters)
		{
			String clusterStr = "";

			for (boolean value : cluster.m_subspace)
			{
				clusterStr = clusterStr + (value ? "1 " : "0 ");
			}
			clusterStr += "[" + cluster.m_objects.size() + "] ";
			for (int value : cluster.m_objects)
			{
				clusterStr += value + " ";
			}
			System.out.println(clusterStr.substring(0, clusterStr.length() - 1));
		}
	}
}
