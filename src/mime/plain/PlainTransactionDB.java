/* A database of transactions.
 *  
 * Copyright (C) 2011 - 2013  Emin Aksehirli, Sandy Moens
 *
 * This file is part of MIME Framework - http://adrem.ua.ac.be/mime . 
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

package mime.plain;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlainTransactionDB
{
	private static final String Delimiter = " ";

	private String databaseName;

	private final List<PlainTransaction> transactions = new LinkedList<PlainTransaction>();

	private final PlainItemDB itemsDB;

	public PlainTransactionDB()
	{
		itemsDB = new PlainItemDB();
	}

	public PlainTransactionDB(String fileName)
	{
		this();
		populateFromFile(fileName);
	}

	private void populateFromFile(String fileName)
			throws IllegalArgumentException
	{
		if (fileName.contains(File.separator))
		{
			databaseName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		} else
		{
			databaseName = fileName;
		}

		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(fileName));
		} catch (FileNotFoundException e)
		{
			throw new IllegalArgumentException(e);
		}

		int transactionCounter = 0;
		while (sc.hasNext())
		{
			String line = sc.nextLine();
			if (line.length() == 0)
			{
				continue;
			}
			String[] splittedLine = line.split(Delimiter);

			// PlainTransaction tx = new PlainTransaction();
			for (String itemStr : splittedLine)
			{
				PlainItem item = itemsDB.get(itemStr);
				item.setTID(transactionCounter);
				// item.setTID(transactions.size());
				// tx.add(item);
			}
			transactionCounter++;
			// transactions.add(tx);
		}
	}

	public String getName()
	{
		return databaseName;
	}

	public PlainItem getItem(int itemId)
	{
		return itemsDB.get(itemId);
	}

	public PlainItem getItem(String itemId)
	{
		return itemsDB.get(itemId);
	}

	public void add(PlainTransaction plainTransaction)
	{
		for (PlainItem item : plainTransaction)
		{
			item.setTID(transactions.size());
		}
		transactions.add(plainTransaction);
	}

	public List<PlainTransaction> getTransactions()
	{
		return unmodifiableList(transactions);
	}

	public PlainItemDB getItemDB()
	{
		return itemsDB;
	}

	public int getNumberOfTransactions()
	{
		return transactions.size();
	}

	public int getNumberOfItems()
	{
		return itemsDB.size();
	}
}
