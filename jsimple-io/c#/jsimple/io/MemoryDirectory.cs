using System;
using System.Collections.Generic;

namespace jsimple.io
{

	using PlatformUtils = jsimple.util.PlatformUtils;


	/// <summary>
	/// @author Bret Johnson
	/// @since 3/23/13 1:49 PM
	/// </summary>
	public class MemoryDirectory : Directory
	{
		private MemoryDirectory parent;
		private string name;
		private long lastModifiedTime;
		private List<MemoryDirectory> subdirectories = new List<MemoryDirectory>();
		private List<MemoryFile> files = new List<MemoryFile>();

		public static MemoryDirectory createRootDirectory()
		{
			return new MemoryDirectory(null, "ROOT");
		}

		private MemoryDirectory(MemoryDirectory parent, string name)
		{
			this.parent = parent;
			this.name = name;
			this.lastModifiedTime = PlatformUtils.CurrentTimeMillis;
		}

		/// <summary>
		/// Get the file, which must already exist under the directory.  If the file doesn't exist, the results are undefined
		/// (for some implementations it will fail right away & others will fail later, like when open it for read).
		/// </summary>
		/// <param name="fileName"> file name </param>
		/// <returns> File object, that's a child of this directory </returns>
		public override File getFile(string fileName)
		{
			foreach (MemoryFile memoryFile in files)
			{
				if (memoryFile.Name.Equals(fileName))
					return memoryFile;
			}

			return new MemoryFile(this, fileName);
		}

		/// <summary>
		/// Add the specified file to this directory, replacing a file with the same name if it already exists.  This method
		/// is called from the close handler on the OutputStream returned from MemoryFile.openForCreate, as that's the public
		/// API for creating/updating a file.
		/// </summary>
		/// <param name="file"> MemoryFile to add/replace </param>
		internal virtual void addOrReplaceFile(MemoryFile file)
		{
			string fileName = file.Name;

			int length = files.Count;
			for (int i = 0; i < length; i++)
			{
				if (files[i].Name.Equals(fileName))
				{
					files[i] = file;
					return;
				}
			}

			files.Add(file);
		}

		/// <summary>
		/// Delete the specified file name, if it exists.
		/// </summary>
		/// <param name="name"> file name </param>
		/// <returns> true if file found and deleted; false if it wasn't found </returns>
		internal virtual bool deleteFile(string name)
		{
			foreach (MemoryFile memoryFile in files)
			{
				if (memoryFile.Name.Equals(name))
				{
					files.Remove(memoryFile);
					return true;
				}
			}

			return false;
		}

		/// <summary>
		/// Get the child directory, which must already exist under this directory.  If the directory doesn't exist, the
		/// results are undefined (for some implementations it will fail right away & others will fail later, like when visit
		/// the children of the directory or open a file).
		/// </summary>
		/// <param name="name"> directory name </param>
		/// <returns> Directory object, that's a child of this directory </returns>
		public override Directory getDirectory(string name)
		{
			foreach (MemoryDirectory subdirectory in subdirectories)
			{
				if (subdirectory.Name.Equals(name))
					return subdirectory;
			}

			throw new PathNotFoundException("MemoryDirectory {} not found", name);
		}

		/// <summary>
		/// Get the child directory, creating it if it doesn't already exist.  For all implementations, the persistent
		/// directory will actually exist after this method returns.
		/// </summary>
		/// <param name="name"> directory name </param>
		/// <returns> child directory </returns>
		public override Directory getOrCreateDirectory(string name)
		{
			foreach (MemoryDirectory memoryDirectory in subdirectories)
			{
				if (memoryDirectory.Name.Equals(name))
					return memoryDirectory;
			}

			MemoryDirectory newMemoryDirectory = new MemoryDirectory(this, name);
			subdirectories.Add(newMemoryDirectory);
			return newMemoryDirectory;
		}

		public virtual void deleteDirectory(string name)
		{
			foreach (MemoryDirectory memoryDirectory in subdirectories)
			{
				if (memoryDirectory.Name.Equals(name))
				{
					subdirectories.Remove(memoryDirectory);
					return;
				}
			}

			throw new PathNotFoundException("MemoryDirectory {} not found", name);
		}

		/// <summary>
		/// Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
		/// visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
		/// if they want to visit all descendants.
		/// </summary>
		public override void visitChildren(DirectoryVisitor visitor)
		{
			foreach (MemoryFile file in files)
				visitor.visit(file, new MemoryPathAttributes(file.LastModifiedTimeInternal, file.Size));

			foreach (MemoryDirectory subdirectory in subdirectories)
				visitor.visit(subdirectory, new MemoryPathAttributes(subdirectory.LastModifiedTime, 0));
		}

		/// <summary>
		/// Get the name of this file/directory--the last component of the path.
		/// </summary>
		/// <returns> name of this file/directory </returns>
		public override string Name
		{
			get
			{
				return name;
			}
		}

		public virtual long LastModifiedTime
		{
			get
			{
				return lastModifiedTime;
			}
		}

		public override void delete()
		{
			if (parent == null)
				throw new Exception("Can't delete root directory");
			parent.deleteDirectory(name);
		}

		private class MemoryPathAttributes : PathAttributes
		{
			internal long lastModifiedTime;
			internal long size;

			public MemoryPathAttributes(long lastModifiedTime, long size)
			{
				this.lastModifiedTime = lastModifiedTime;
				this.size = size;
			}

			/// <returns> last time the directory/file was modified, in millis. </returns>
			public override long LastModifiedTime
			{
				get
				{
					return lastModifiedTime;
				}
			}

			/// <returns> for a file its size in bytes and for a directory the results are implementation specific </returns>
			public override long Size
			{
				get
				{
					return 0;
				}
			}
		}
	}

}