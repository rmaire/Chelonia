/* 
 * Copyright 2020 Uprise Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.uprisesoft.chelonia.turtle;

import ch.uprisesoft.yali.ast.node.List;
import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.ast.node.Procedure;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.procedures.ProcedureProvider;
import ch.uprisesoft.yali.scope.Scope;
import com.badlogic.gdx.graphics.Color;
import java.util.Arrays;

/**
 *
 * @author uprisesoft@gmail.com
 */
public class TurtleManager implements ProcedureProvider {

    Turtle turtle = new Turtle();

    public Turtle getTurtle() {
        return turtle;
    }

    public Node fd(Scope scope, java.util.List<Node> args) {
        Node arg = args.get(0);
        if (!(arg.type().equals(NodeType.INTEGER) || arg.type().equals(NodeType.INTEGER))) {
            throw new NodeTypeException(arg, arg.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (arg.type().equals(NodeType.FLOAT)) {
            turtle.fd(args.get(0).toFloatWord().getFloat().intValue());
        }

        if (arg.type().equals(NodeType.INTEGER)) {
            turtle.fd(args.get(0).toIntegerWord().getInteger());
        }

        return turtlepos();
    }

    public Node bk(Scope scope, java.util.List<Node> args) {
        Node arg = args.get(0);
        if (!(arg.type().equals(NodeType.INTEGER) || arg.type().equals(NodeType.INTEGER))) {
            throw new NodeTypeException(arg, arg.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (arg.type().equals(NodeType.FLOAT)) {
            turtle.bk(args.get(0).toFloatWord().getFloat().intValue());
        }

        if (arg.type().equals(NodeType.INTEGER)) {
            turtle.bk(args.get(0).toIntegerWord().getInteger());
        }

        return turtlepos();
    }

    public Node lt(Scope scope, java.util.List<Node> args) {
        Node arg = args.get(0);
        if (!(arg.type().equals(NodeType.INTEGER) || arg.type().equals(NodeType.INTEGER))) {
            throw new NodeTypeException(arg, arg.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (arg.type().equals(NodeType.FLOAT)) {
            turtle.lt(args.get(0).toFloatWord().getFloat().intValue());
        }

        if (arg.type().equals(NodeType.INTEGER)) {
            turtle.lt(args.get(0).toIntegerWord().getInteger());
        }
        return turtlepos();
    }

    public Node rt(Scope scope, java.util.List<Node> args) {
        Node arg = args.get(0);
        if (!(arg.type().equals(NodeType.INTEGER) || arg.type().equals(NodeType.FLOAT))) {
            throw new NodeTypeException(arg, arg.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (arg.type().equals(NodeType.FLOAT)) {
            turtle.rt(args.get(0).toFloatWord().getFloat().intValue());
        }

        if (arg.type().equals(NodeType.INTEGER)) {
            turtle.rt(args.get(0).toIntegerWord().getInteger());
        }

        return turtlepos();
    }

    public Node cs(Scope scope, java.util.List<Node> args) {
        turtle.cs();
        Node posCheat = Node.list(Arrays.asList(Node.flt(0d),Node.flt(0d),Node.flt(0d)));
        return posCheat;
    }

    public Node st(Scope scope, java.util.List<Node> args) {
        turtle.st();
        return turtlepos();
    }

    public Node ht(Scope scope, java.util.List<Node> args) {
        turtle.ht();
        return turtlepos();
    }

    public Node pd(Scope scope, java.util.List<Node> args) {
        turtle.pd();
        return turtlepos();
    }

    public Node pu(Scope scope, java.util.List<Node> args) {
        turtle.pu();
        return turtlepos();
    }

    public Node ts(Scope scope, java.util.List<Node> args) {
        Node arg = args.get(0);
        if (!(arg.type().equals(NodeType.INTEGER) || arg.type().equals(NodeType.FLOAT))) {
            throw new NodeTypeException(arg, arg.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (arg.type().equals(NodeType.FLOAT)) {
            turtle.ts(args.get(0).toFloatWord().getFloat().intValue());
        }

        if (arg.type().equals(NodeType.INTEGER)) {
            turtle.ts(args.get(0).toIntegerWord().getInteger());
        }

        return turtlepos();
    }

    public Node setxy(Scope scope, java.util.List<Node> args) {
        Node x = args.get(0);
        Node y = args.get(1);

        double xf;
        double yf;

        if (x.type().equals(NodeType.FLOAT)) {
            xf = x.toFloatWord().getFloat();
        } else if (x.type().equals(NodeType.INTEGER)) {
            xf = x.toIntegerWord().getInteger();
        } else {
            throw new NodeTypeException(x, x.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        if (y.type().equals(NodeType.FLOAT)) {
            yf = y.toFloatWord().getFloat();
        } else if (y.type().equals(NodeType.INTEGER)) {
            yf = y.toIntegerWord().getInteger();
        } else {
            throw new NodeTypeException(y, y.type(), NodeType.INTEGER, NodeType.FLOAT);
        }

        turtle.setxy(xf, yf);

        return turtlepos();
    }

    public Node setpc(Scope scope, java.util.List<Node> args) {
        String color = args.get(0).toQuotedWord().getQuote().toLowerCase();

        switch (color) {
            case "red":
                turtle.setpc(Color.RED);
                break;
            case "green":
                turtle.setpc(Color.GREEN);
                break;
            case "blue":
                turtle.setpc(Color.BLUE);
                break;
            case "yellow":
                turtle.setpc(Color.YELLOW);
                break;
            case "white":
                turtle.setpc(Color.WHITE);
                break;
        }

        return turtlepos();
    }

    public Node turtlepos(Scope scope, java.util.List<Node> args) {
        return turtlepos();
    }

    private Node turtlepos() {

        TurtlePosition tp = turtle.getPosition();
        List pos = new List();
        pos.addChild(Node.flt(Double.valueOf(tp.x)));
        pos.addChild(Node.flt(Double.valueOf(tp.y)));
        pos.addChild(Node.flt(Double.valueOf(tp.angle)));

        return pos;
    }

    @Override
    public Interpreter registerProcedures(Interpreter it) {
        it.env().define(new Procedure("fd", (scope, val) -> this.fd(scope, val), (scope, val) -> Node.none(), "__dist__"));
        it.env().define(new Procedure("bk", (scope, val) -> this.bk(scope, val), (scope, val) -> Node.none(), "__dist__"));
        it.env().define(new Procedure("rt", (scope, val) -> this.rt(scope, val), (scope, val) -> Node.none(), "__angle__"));
        it.env().define(new Procedure("lt", (scope, val) -> this.lt(scope, val), (scope, val) -> Node.none(), "__angle__"));
        it.env().define(new Procedure("cs", (scope, val) -> this.cs(scope, val), (scope, val) -> Node.none()));
        it.env().define(new Procedure("st", (scope, val) -> this.st(scope, val), (scope, val) -> Node.none()));
        it.env().define(new Procedure("ht", (scope, val) -> this.ht(scope, val), (scope, val) -> Node.none()));
        it.env().define(new Procedure("pd", (scope, val) -> this.pd(scope, val), (scope, val) -> Node.none()));
        it.env().define(new Procedure("pu", (scope, val) -> this.pu(scope, val), (scope, val) -> Node.none()));
        it.env().define(new Procedure("setxy", (scope, val) -> this.setxy(scope, val), (scope, val) -> Node.none(), "__x__", "__y__"));
        it.env().define(new Procedure("setpc", (scope, val) -> this.setpc(scope, val), (scope, val) -> Node.none(), "__color__"));
        it.env().define(new Procedure("ts", (scope, val) -> this.ts(scope, val), (scope, val) -> Node.none(), "__speed__"));
        it.env().define(new Procedure("turtlepos", (scope, val) -> this.turtlepos(scope, val), (scope, val) -> Node.none()));
        return it;
    }
}
