using System.Collections.Generic;
using System.Text;

namespace jsimple.util
{

	/// <summary>
	/// A TextualPath abstracts any path-like structure that can be represented as a list of strings.  It can be used for
	/// paths in a file system, a URL path, an XPath path, etc.  Implementation wise, it's simply a list of 0 or move
	/// strings.
	/// <p/>
	/// Unlike the standard Java Path class: This isn't just for file system paths and "." and ".." have no special meaning.
	/// A TextualPath has no concept of being absolute or relative; it's simplest to think about it as always being absolute
	/// and toString represents it that way (with a leading "/"), but if the caller wants to treat it as a relative path it
	/// can do that. Also, "/abc" and "/abc/" have the same representation in a TextualPath--they both are a list with one
	/// element, "abc", which toString will return as "/abc".
	/// 
	/// @author Bret Johnson
	/// @since 8/16/13 9:55 PM
	/// </summary>
	public class TextualPath
	{
		private List<string> pathElements;

		/// <summary>
		/// Create a TextualPath that's empty (that is, points to the root).
		/// </summary>
		public TextualPath()
		{
			this.pathElements = new List<string>();
		}

		/// <summary>
		/// Create a TextualPath containing a single path element.
		/// </summary>
		/// <param name="singlePathElement"> </param>
		public TextualPath(string singlePathElement)
		{
			this.pathElements = new List<string>(1);
			pathElements[0] = singlePathElement;
		}

		/// <summary>
		/// Create a TextualPath that's the same as parent path, except with the new child added at the end.
		/// </summary>
		/// <param name="parent"> parent path </param>
		/// <param name="child">  child element to add at end </param>
		public TextualPath(TextualPath parent, string child)
		{
			pathElements = new List<string>(parent.Length + 1);
			foreach (string pathElement in parent.PathElements)
				pathElements.Add(pathElement);
			pathElements.Add(child);
		}

		public override bool Equals(object o)
		{
			if (!(o is TextualPath))
				return false;
			TextualPath that = (TextualPath) o;
			return pathElements.Equals(that.pathElements);
		}

		public override int GetHashCode()
		{
			return pathElements.GetHashCode();
		}

		public virtual void add(string pathElement)
		{
			pathElements.Add(pathElement);
		}

		/// <summary>
		/// Get the length of the path, the number of path elements contained in it.
		/// </summary>
		/// <returns> path length, which is >= 0 </returns>
		public virtual int Length
		{
			get
			{
				return pathElements.Count;
			}
		}

		/// <summary>
		/// Check if the path is empty, having no elements.  This can be thought of as pointing to the root of the tree.
		/// </summary>
		/// <returns> true if path has no elements </returns>
		public virtual bool Empty
		{
			get
			{
				return pathElements.Count == 0;
			}
		}

		/// <summary>
		/// Get the parent of this path, containing all elements except the last.  If the path is empty and thus has no
		/// parent, an exception is thrown.
		/// </summary>
		/// <returns> parent of this path </returns>
		public virtual TextualPath Parent
		{
			get
			{
				if (Empty)
					throw new BasicException("Can't call getParent on an empty path");
    
				TextualPath newPath = new TextualPath();
				int newSize = pathElements.Count - 1;
				for (int i = 0; i < newSize; i++)
					newPath.add(pathElements[i]);
    
				return newPath;
			}
		}

		public virtual List<string> PathElements
		{
			get
			{
				return pathElements;
			}
		}

		public virtual string LastElement
		{
			get
			{
				int size = pathElements.Count;
				if (size == 0)
					throw new BasicException("Can't call getLastElement on an empty path");
				else
					return pathElements[size - 1];
			}
		}

		/// <summary>
		/// Get a human friendly string representation of the path, using / as the delimiter.  Note that no escaping is done
		/// of special characters (space, slash, etc.), so this string often isn't appropriate for programmatic use.
		/// </summary>
		/// <returns> human friendly string representation of the path </returns>
		public override string ToString()
		{
			if (pathElements.Count == 0)
				return "/";
			else
			{
				StringBuilder buffer = new StringBuilder();

				foreach (string pathElement in pathElements)
				{
					buffer.Append("/");
					buffer.Append(pathElement);
				}
				return buffer.ToString();
			}
		}
	}

}