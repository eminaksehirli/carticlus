/* An utility class.
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
package mime.tool;

public class Pair<A, B>
{
	protected A first;
	protected B second;

	public Pair(A first, B second)
	{
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode()
	{
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Pair)
		{
			Pair<?, ?> otherPair = (Pair<?, ?>) other;
			return ((this.first == otherPair.first || (this.first != null
					&& otherPair.first != null && this.first.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
					&& otherPair.second != null && this.second.equals(otherPair.second))));
		}

		return false;
	}

	@Override
	public String toString()
	{
		return "(" + first + ", " + second + ")";
	}

	public A getFirst()
	{
		return first;
	}

	public void setFirst(A first)
	{
		this.first = first;
	}

	public B getSecond()
	{
		return second;
	}

	public void setSecond(B second)
	{
		this.second = second;
	}
}