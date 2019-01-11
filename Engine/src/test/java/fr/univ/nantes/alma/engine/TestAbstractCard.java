package fr.univ.nantes.alma.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class TestAbstractCard {

  @Test
  public void testGetId() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getId();
    Whitebox.setInternalState(absCard, "idCard", 1);
    assertThat(1).isEqualTo(absCard.getId());
  }
  
  @Test
  public void testGetType() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getType();
    Whitebox.setInternalState(absCard, "type", "mage");
    assertThat("mage").isEqualTo(absCard.getType());
  }
  
  @Test
  public void testGetName() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getName();
    Whitebox.setInternalState(absCard, "name", "Jaina");
    assertThat("Jaina").isEqualTo(absCard.getName());
  }
  
  @Test
  public void testGetManaCost() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getManaCost();
    Whitebox.setInternalState(absCard, "manaCost", 2);
    assertThat(2).isEqualTo(absCard.getManaCost());
  }
  
  @Test
  public void testGetDamage() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getDamage();
    Whitebox.setInternalState(absCard, "damage", 1);
    assertThat(1).isEqualTo(absCard.getDamage());
  }
  
  @Test
  public void testGetDescription() {
    AbstractCard absCard = Mockito.mock(AbstractCard.class);
    PowerMockito.doCallRealMethod().when(absCard).getDescription();
    Whitebox.setInternalState(absCard, "description", "charge");
    assertThat("charge").isEqualTo(absCard.getDescription());
  }

}
