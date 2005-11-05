/* This file was generated by SableCC (http://www.sablecc.org/). */

package l1.node;

import l1.analysis.*;

public final class TIn extends Token
{
    public TIn()
    {
        super.setText("in");
    }

    public TIn(int line, int pos)
    {
        super.setText("in");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TIn(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTIn(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TIn text.");
    }
}
