/* A class that represents transactions.
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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PlainTransaction implements Iterable<PlainItem>
{
	private Set<PlainItem> items = new HashSet<PlainItem>();

	public PlainTransaction(PlainItem... initialItems)
	{
		this.items.addAll(asList(initialItems));
	}

	public void add(PlainItem item)
	{
		items.add(item);
	}

	public boolean contains(PlainItem item)
	{
		return items.contains(item);
	}

	public Collection<PlainItem> asCollection()
	{
		return unmodifiableSet(items);
	}

	@Override
	public Iterator<PlainItem> iterator()
	{
		return asCollection().iterator();
	}

	@Override
	public String toString()
	{
		return items.toString();
	}
}
