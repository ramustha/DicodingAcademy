package com.ramusthastudio.mytestingapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
  private MainView fMainView;
  private MainPresenter fMainPresenter;

  @Before
  public void setUp() throws Exception {
    fMainView = mock(MainView.class);
    fMainPresenter = new MainPresenter(fMainView);
  }

  @Test
  public void testVolumeWithDoubleInput() throws Exception {
    double volume = fMainPresenter.volume(2.3, 8.1, 2.9);
    assertEquals(54.026999999999994, volume, 0.0001);
  }

  @Test
  public void testVolumeWithZeroInput() throws Exception {
    double volume = fMainPresenter.volume(0, 0, 0);
    assertEquals(0.0, volume , 0.0001);
  }

  @Test
  public void testHitungVolume() throws Exception {
    fMainPresenter.hitungVolume(11.1, 2.2, 1);
    verify(fMainView).showVolume(any(MainModel.class));
  }
}