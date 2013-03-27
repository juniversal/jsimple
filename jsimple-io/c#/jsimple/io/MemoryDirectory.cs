using System.Collections.Generic;

namespace jsimple.io
{




	/// <summary>
	/// @author Bret Johnson
	/// @since 3/23/13 1:49 PM
	/// </summary>
	public class MemoryDirectory : Directory
	{
		private string name;
		private long lastModificationTime;
		private List<MemoryDirectory> subdirectories = new List<MemoryDirectory>();
		private List<MemoryFile> files = new List<MemoryFile>();

		public MemoryDirectory(string name)
		{
			this.name = name;
		}

		/// <summary>
		/// Get the file, which must already exist under the directory.  If the file doesn't exist, the results are undefined
		/// (for some implementations it will fail right away & others will fail later, like when open it for read).
		/// </summary>
		/// <param name="name"> file name </param>
		/// <returns> File object, that's a child of this directory </returns>
		public override File getFile(string name)
		{
			foreach (MemoryFile memoryFile in files)
			{
				if (memoryFile.Name.Equals(name))
					return memoryFile;
			}

			throw new FileNotFoundException("MemoryFile " + name + " not found");
		}

		/// <summary>
		/// Create a new file under this directory.  If a file with that name already exists, it will be overwritten.
		/// File.openForCreate must be called at some point after this method to actually open the file; these methods must be
		/// called as a pair--if one is called without the other, the results are undefined (meaning they are different for
		/// different implementations). Some implementations actually create an empty file when this is called, while others
		/// delay file creation until File.openForCreate is called and the contents are written (which is the preferred
		/// implementation, as it's generally more efficient).
		/// <p/>
		/// </summary>
		/// <param name="name"> file name </param>
		/// <returns> File object, that's a child of this directory </returns>
		public override File createFile(string name)
		{
			foreach (MemoryFile memoryFile in files)
			{
				if (memoryFile.Name.Equals(name))
					return memoryFile;
			}

			MemoryFile newMemoryFile = new MemoryFile(name);
			files.Add(newMemoryFile);
			return newMemoryFile;
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

			throw new FileNotFoundException("MemoryDirectory " + name + " not found");
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

			MemoryDirectory newMemoryDirectory = new MemoryDirectory(name);
			subdirectories.Add(newMemoryDirectory);
			return newMemoryDirectory;
		}

		/// <summary>
		/// Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
		/// visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
		/// if they want to visit all descendants.
		/// </summary>
		public override void visitChildren(DirectoryVisitor visitor)
		{
			foreach (MemoryFile file in files)
				visitor.visit(file, new MemoryPathAttributes(file.LastModificationTime, file.Size));

			foreach (MemoryDirectory subdirectory in subdirectories)
				visitor.visit(subdirectory, new MemoryPathAttributes(subdirectory.LastModificationTime, 0));
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

		public virtual long LastModificationTime
		{
			get
			{
				return lastModificationTime;
			}
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
					return 0; //To change body of implemented methods use File | Settings | File Templates.
				}
			}

			/// <returns> for a file its size in bytes and for a directory the results are implementation specific </returns>
			public override long Size
			{
				get
				{
					return 0; //To change body of implemented methods use File | Settings | File Templates.
				}
			}
		}
	}

}