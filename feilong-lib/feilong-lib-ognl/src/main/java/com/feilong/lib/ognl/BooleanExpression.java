/**
 * 
 */
package com.feilong.lib.ognl;

import com.feilong.lib.ognl.enhance.UnsupportedCompilationException;

/**
 * Base class for boolean expressions.
 * 
 * @author jkuhnert
 */
public abstract class BooleanExpression extends ExpressionNode implements NodeType{

    /**
     * 
     */
    private static final long serialVersionUID = -3641594575348908917L;

    protected Class           _getterClass;

    public BooleanExpression(int id){
        super(id);
    }

    public BooleanExpression(OgnlParser p, int id){
        super(p, id);
    }

    @Override
    public Class getGetterClass(){
        return _getterClass;
    }

    @Override
    public Class getSetterClass(){
        return null;
    }

    @Override
    public String toGetSourceString(OgnlContext context,Object target){
        try{

            Object value = getValueBody(context, target);

            if (value != null && Boolean.class.isAssignableFrom(value.getClass())){
                _getterClass = Boolean.TYPE;
            }else if (value != null){
                _getterClass = value.getClass();
            }else{
                _getterClass = Boolean.TYPE;
            }

            String ret = super.toGetSourceString(context, target);

            if ("(false)".equals(ret)){
                return "false";
            }else if ("(true)".equals(ret)){
                return "true";
            }

            return ret;

        }catch (NullPointerException e){

            // expected to happen in some instances
            e.printStackTrace();

            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        }catch (Throwable t){
            throw OgnlOps.castToRuntime(t);
        }
    }
}
