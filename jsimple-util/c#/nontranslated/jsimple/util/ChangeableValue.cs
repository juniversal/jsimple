using System;
using System.ComponentModel;

namespace jsimple.util
{
    public class ChangeableValue<T> : ChangeableValueBase<T> , INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        public override T Value
        {
            get { throw new System.NotImplementedException(); }
        }

        public override void changed()
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (handler != null)
                handler(this, new PropertyChangedEventArgs("Value"));
        }
    }
}