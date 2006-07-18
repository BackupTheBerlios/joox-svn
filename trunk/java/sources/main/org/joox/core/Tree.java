/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author bert
 * 
 */
@SuppressWarnings("nls")
public class Tree implements Map
{
	/**
	 * 
	 */
	protected String separator = "\\."; // Regular Expression

	/**
	 * 
	 */
	protected Map<String, Object> tree = new TreeMap<String, Object>();

	/**
	 * 
	 */
	public Tree()
	{
		// tree.put(this.hashCode() + ".regex", this.separator);
	}

	/**
	 * @param string
	 */
	public Tree(XMLString string)
	{
		String xml = string.getXml();
		// TODO implement
	}

	/**
	 * @param regex
	 */
	public Tree(RegEx regex)
	{
		this.separator = regex.getRegEx();
		// tree.put(this.hashCode() + ".regex", this.separator);
	}

	/**
	 * @return
	 */
	public String toXML()
	{
		String result = null;
		// TODO implement (key as tags, values as content)
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.tree.toString();
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		this.tree.clear();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		return this.tree.entrySet();
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object keyobj)
	{
		String key = "" + keyobj; // its now a string also if it was null
		String[] token = key.split(this.separator);
		Object result = null;
		Map<String, Object> currenttree = this.tree;
		for (int i = 0; i < token.length; i++)
		{
			int hashcode = this.hashCode();
			Object value = currenttree.get(token[i]);
			if (value != null && value instanceof Tree)
			{ // known path
				currenttree = ((Tree) value).getMap();
				hashcode = value.hashCode();
			}
			else if (i != token.length - 1)
			{ // unknown
				result = null;
				break;
			}

			if (i == token.length - 1)
			{ // end of path reached
				result = currenttree.get(hashcode + ".value");
				if (result == null)
					result = currenttree;
			}
		}
		return result;
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return this.tree.isEmpty();
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet()
	{
		return this.tree.keySet();
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object keyobj, Object newvalue)
	{
		return put(keyobj, newvalue, this.separator);
	}

	/**
	 * @param keyobj
	 * @param newvalue
	 * @param regex
	 * @return
	 * 
	 */
	public Object put(Object keyobj, Object newvalue, String regex)
	{
		String key = "" + keyobj; // its now a string also if it was null
		String[] token = key.split(regex);
		Object result = null;

		Map<String, Object> currenttree = this.tree;
		int hashcode = this.hashCode();
		for (int i = 0; i < token.length; i++)
		{
			boolean keyexist = false;
			Object value = currenttree.get(token[i]);
			if (value != null && value instanceof Tree)
			{ // known path
				currenttree = ((Tree) value).getMap();
				hashcode = value.hashCode();
				keyexist = true;
			}
			else if (i != token.length - 1)
			{ // new path
				Tree newtree = new Tree(new RegEx(regex));
				currenttree.put(token[i], newtree);
				currenttree = newtree.tree;
				hashcode = newtree.hashCode();
			}

			if (i == token.length - 1)
			{ // end of path reached
				if (keyexist)
					result = currenttree.put(hashcode + ".value", newvalue);
				else
				{
					Tree targettree = new Tree(new RegEx(regex));
					targettree.tree.put(targettree.hashCode() + ".value", newvalue);
					result = currenttree.put(token[i], targettree);
				}
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	private Map<String, Object> getMap()
	{
		return this.tree;
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map map)
	{
		this.tree.putAll(map);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		// TODO Auto-generated method stub
		return this.tree.size();
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values()
	{
		return this.tree.values();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Tree tree = new Tree();
		tree.put("a1.b1.c1.d1", "1a1.b1.c1.d1");
		tree.put("a1.b1", "2a1.b1");
		tree.put("a1.b2.c1", "3a1.b2.c1");
		tree.put("a2.b1.c1", "4a2.b1.c1");

		System.out.println(tree.get("a1"));
		System.out.println(tree.get("a1.b1"));
		System.out.println(tree.get("a2"));
		System.out.println(tree.get("a1.b1.c1"));
		System.out.println(tree.get("a1.b1.c1.d1"));
	}
}
