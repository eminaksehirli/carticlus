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
import static java.lang.Integer.parseInt;
import static java.lang.Math.ceil;
import i9.subspace.base.Cluster;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class CartiClus
{
	public static final int N_Threads = (int) ceil(0.75 * Runtime.getRuntime()
			.availableProcessors());

	public static void main(String[] args) throws Exception
	{
		if (args.length < 4)
		{
			System.out.println("Usage: java -jar carticlus.jar data-file k minsup numOfdimensions [cartLog] [outputfile]");
			System.out.println();
			System.out.println("data-file: Path to the multi dimensional datafile that will be cartified");
			System.out.println("k: Parameter for the _k_ nearest neighbors");
			System.out.println("minsup: minimum support count for the mining");
			System.out.println("numOfdimensions: The number of dimensions in the data-file");
			System.out.println("cartLog: (optional) Direct the output of the mining step instead of /dev/null");
			System.out.println("outputfile: (optional) Direct output to this file instead of standart output");
			System.out.println();
			System.out.println("For more information, please check the web-site: http://adrem.ua.ac.be/cartification");
			return;
		}

		String dataFile = args[0];
		final int k = parseInt(args[1]);
		final int minSup = parseInt(args[2]);
		final int numOfDimensions = parseInt(args[3]);

		String cartLogFile = "";
		if (args.length > 4)
		{
			cartLogFile = args[4];
		}

		String outFile = "";
		if (args.length > 5)
		{
			outFile = args[5];
		}

		PrintWriter cartLog;
		if (cartLogFile.length() > 1)
		{
			cartLog = new PrintWriter(new BufferedWriter(new FileWriter(cartLogFile)));
		} else
		{
			cartLog = new PrintWriter(nullOutputStream());
		}

		PrintStream outWriter = System.out;
		if (outFile.length() > 1)
		{
			outWriter = new PrintStream(new FileOutputStream(outFile));
		}

		System.out.println("Running: " + " dataFile: " + dataFile + ", k: " + k
				+ ", minSup: " + minSup + ", numOfDimensions: " + numOfDimensions
				+ ", cartLog: " + cartLogFile + ", outputFile:" + outFile);

		ClusterFinder cartRunner = new ClusterFinder(numOfDimensions,
				k,
				minSup,
				cartLog);

		cartLog.println("====== " + dataFile + " ======");
		List<Cluster> cartClusters = cartRunner.run(dataFile);

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
			outWriter.println(clusterStr.substring(0, clusterStr.length() - 1));
		}
	}
}
