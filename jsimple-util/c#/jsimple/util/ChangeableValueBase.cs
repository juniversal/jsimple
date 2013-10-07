namespace jsimple.util
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 10/6/13 3:24 PM
	/// </summary>
	public abstract class ChangeableValueBase<T>
	{
		public abstract T Value {get;}
		public abstract void changed();
	}

}