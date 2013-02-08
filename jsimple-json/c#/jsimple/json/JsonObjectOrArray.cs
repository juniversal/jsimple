namespace jsimple.json
{

	/// <summary>
	/// This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
	/// a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
	/// 
	/// @author Bret Johnson
	/// @since 7/8/12 1:56 PM
	/// </summary>
	public abstract class JsonObjectOrArray
	{
		public override string ToString()
		{
			return Json.serialize(this);
		}
	}

}