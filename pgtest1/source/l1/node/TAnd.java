/* This file was generated by SableCC (http://www.sablecc.org/). */

package l1.node;

import l1.analysis.*;

public final class TAnd extends Token
{
    public TAnd()
    {
        super.setText("&&");
    }

    public TAnd(int line, int pos)
    {
        super.setText("&&");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TAnd(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTAnd(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TAnd text.");
    }
}
