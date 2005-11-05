/* This file was generated by SableCC (http://www.sablecc.org/). */

package l1.node;

import java.util.*;
import l1.analysis.*;

public final class AApplicationExpression extends PExpression
{
    private PExpression _e0_;
    private PExpression _e1_;

    public AApplicationExpression()
    {
    }

    public AApplicationExpression(
        PExpression _e0_,
        PExpression _e1_)
    {
        setE0(_e0_);

        setE1(_e1_);

    }
    public Object clone()
    {
        return new AApplicationExpression(
            (PExpression) cloneNode(_e0_),
            (PExpression) cloneNode(_e1_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAApplicationExpression(this);
    }

    public PExpression getE0()
    {
        return _e0_;
    }

    public void setE0(PExpression node)
    {
        if(_e0_ != null)
        {
            _e0_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _e0_ = node;
    }

    public PExpression getE1()
    {
        return _e1_;
    }

    public void setE1(PExpression node)
    {
        if(_e1_ != null)
        {
            _e1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _e1_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_e0_)
            + toString(_e1_);
    }

    void removeChild(Node child)
    {
        if(_e0_ == child)
        {
            _e0_ = null;
            return;
        }

        if(_e1_ == child)
        {
            _e1_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_e0_ == oldChild)
        {
            setE0((PExpression) newChild);
            return;
        }

        if(_e1_ == oldChild)
        {
            setE1((PExpression) newChild);
            return;
        }

    }
}
