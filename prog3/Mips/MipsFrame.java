package Mips;

import java.util.Hashtable;
import Symbol.Symbol;
import Temp.Temp;
import Temp.Label;
import Frame.Frame;
import Frame.Access;
import Frame.AccessList;

public class MipsFrame extends Frame {
  private int count = 0;
  private int localOffset = 0; // Offset for local variables in the frame

  public Frame newFrame(Symbol name, Util.BoolList formals) {
    Label label;
    if (name == null)
      label = new Label();
    else if (this.name != null)
      label = new Label(this.name + "." + name + "." + count++);
    else
      label = new Label(name);
    return new MipsFrame(label, formals);
  }

  public MipsFrame() {
  }

  private MipsFrame(Label n, Util.BoolList f) {
    name = n;
    // Allocate formal parameters
    allocFormals(f);
  }

  private static final int wordSize = 4;

  public int wordSize() {
    return wordSize;
  }

  /**
   * Allocate a local variable in the frame.
   * If escape is true, allocate in memory (InFrame).
   * If escape is false, allocate in a register (InReg).
   */
  public Access allocLocal(boolean escape) {
    if (escape) {
      // Allocate in frame (memory)
      // Local variables grow downward from frame pointer
      Access acc = new Mips.InFrame(localOffset);
      localOffset -= wordSize; // Move to next slot (negative direction)
      return acc;
    } else {
      // Allocate in register (temporary)
      return new Mips.InReg(new Temp());
    }
  }

  /**
   * Allocate formal parameters based on escape information.
   * MIPS calling convention:
   * - First 4 arguments in registers $a0-$a3
   * - Additional arguments on stack
   * - Space is always allocated in caller's frame for all formals
   */
  private void allocFormals(Util.BoolList formals) {
    int formalOffset = 0; // Offset in caller's outgoing argument area
    int regCount = 0; // Count of register parameters (max 4 on MIPS)

    AccessList prevList = null;
    for (Util.BoolList fl = formals; fl != null; fl = fl.tail) {
      Access acc;

      if (fl.head) {
        // Formal parameter escapes - must be in frame
        acc = new Mips.InFrame(formalOffset);
      } else {
        // Formal parameter doesn't escape - can use register
        // But still allocate space in caller's frame
        if (regCount < 4) {
          // Use a register for non-escaping formals
          acc = new Mips.InReg(new Temp());
        } else {
          // Beyond 4 parameters, must use frame
          acc = new Mips.InFrame(formalOffset);
        }
      }

      // Build the formal access list
      AccessList newList = new AccessList(acc, null);
      if (prevList == null) {
        this.formals = newList;
      } else {
        prevList.tail = newList;
      }
      prevList = newList;

      formalOffset += wordSize; // Move to next argument slot
      regCount++;
    }
  }

}