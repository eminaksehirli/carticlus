/* A class that represents items.
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

import java.util.BitSet;

public class PlainItem implements Comparable<PlainItem>
{
	int id;
	private final BitSet tids;
	private String name;

	public PlainItem(int id)
	{
		this.id = id;
		tids = new BitSet();
		name = String.valueOf(id);
	}

	public PlainItem(int id, BitSet tids)
	{
		this(id);
		this.tids.or(tids);
	}

	public PlainItem(int id, String name)
	{
		this(id);
		setName(name);
	}

	public void setTID(int txCounter)
	{
		tids.set(txCounter);
	}

	@Override
	public int compareTo(PlainItem o)
	{
		return id - o.id;
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlainItem other = (PlainItem) obj;
		if (id == other.id)
			return true;
		return false;
	}

	public BitSet getTIDs()
	{
		return tids;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
