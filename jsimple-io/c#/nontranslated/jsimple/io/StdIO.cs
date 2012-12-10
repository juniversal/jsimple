using System;
using System.Diagnostics;

namespace jsimple.io
{
    public class StdIO
    {
        public static Writer stdout = new DebugWriter();
        public static Writer stderr = stdout;
    }
}
