/* Finds some subspace clusters using cartification transformation. 
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

import static com.google.common.collect.Sets.intersection;
import static com.google.common.io.ByteStreams.nullOutputStream;
import i9.subspace.base.Cluster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import mime.plain.PlainItem;
import mime.plain.PlainItemset;
import mime.plain.PlainTransactionDB;
import mime.plain.measure.SupportMeasure;
import mime.workers.assoc.RandomMaximalMiner;

public class ClusterFinder
{
	private final int numOfDimensions;
	private PrintWriter cartLog;
	private PlainTransactionDB trDb;
	private int minSize;
	private final int k;
	private final int minSup;

	public ClusterFinder(int numOfDimensions, int k, int minSup,
			PrintWriter cartLog)
	{
		this.numOfDimensions = numOfDimensions;
		this.k = k;
		this.minSup = minSup;
		this.cartLog = cartLog;
	}

	public ClusterFinder(int numOfDimensions, int k, int minSup)
	{
		this(numOfDimensions, k, minSup, new PrintWriter(nullOutputStream()));
	}

	public List<Cluster> run(String mimeFile)
	{
		String cartifiedFile = CartifierDriver.cartify(mimeFile, k);
		trDb = new PlainTransactionDB(cartifiedFile);
		minSize = k / 2;
		cartLog.println("k: " + k + ", MinSup: " + minSup + ", minSize: " + minSize);
		List<PlainItemset> raw_mfis = RandomMaximalMiner.runParallel(
				trDb.getItemDB(), minSup, 100, new SupportMeasure(), minSize);

		for (PlainItemset fis : raw_mfis)
		{
			cartLog.println(fis.size() + ": " + fis);
		}
		cartLog.flush();

		List<PlainItemset> mfis = mergeClusters(raw_mfis);

		List<Cluster> cartClusters = getClusters(mfis);

		return cartClusters;
	}

	private List<PlainItemset> mergeClusters(List<? extends PlainItemset> raw_mfis)
	{
		if (raw_mfis.size() <= 1)
		{
			return (List<PlainItemset>) raw_mfis;
		}
		ArrayList<PlainItemset> mfis = new ArrayList<PlainItemset>();

		mfis.add(raw_mfis.get(0));

		outer:
		for (PlainItemset raw_fis : raw_mfis)
		{
			for (PlainItemset fis : mfis)
			{
				if (intersection(fis.asCollection(), raw_fis.asCollection()).size() >= minSize)
				{
					fis.addAll(raw_fis);
					continue outer;
				}
			}
			mfis.add(raw_fis);
		}
		return mfis;
	}

	private List<Cluster> getClusters(Collection<PlainItemset> fises)
	{
		final int numOfItems = trDb.getNumberOfItems();
		List<Cluster> clusters = new ArrayList<Cluster>(fises.size());
		for (PlainItemset fis : fises)
		{
			List<Integer> cluster = new ArrayList<Integer>(fis.size());

			for (PlainItem item : fis)
			{
				cluster.add(Integer.valueOf(item.getName()));
			}

			boolean[] subspace = new boolean[numOfDimensions];
			for (int i = 0; i < subspace.length; i++)
			{
				BitSet subSpaceMask = new BitSet(numOfDimensions * numOfItems);
				subSpaceMask.set(i * numOfItems, (i + 1) * numOfItems);

				subSpaceMask.and(fis.getTIDs());

				if (subSpaceMask.cardinality() > 0)
				{
					subspace[i] = true;
				}
			}
			clusters.add(new Cluster(subspace, cluster));
		}
		return clusters;
	}
}
