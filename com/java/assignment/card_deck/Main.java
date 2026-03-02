package com.java.assignment.card_deck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    Deck uiDeck = new Deck();
    List<Card> uiDrawn = new ArrayList<>();

    int animX = -100;
    int animY = -100;
    Card animCard = null;
    javax.swing.Timer t;

    JPanel p;

    public Main() {
        uiDeck.shuffle();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        p = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(30, 120, 30));
                g.fillRect(0, 0, 800, 600);

                if (uiDeck.size() > 0) {
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(50, 50, 70, 100, 10, 10);
                    g.setColor(Color.BLUE);
                    g.fillRect(55, 55, 60, 90);
                }

                for (int i = 0; i < uiDrawn.size(); i++) {
                    drawC(g, uiDrawn.get(i), 200 + (i % 10) * 50, 50 + (i / 10) * 110);
                }

                if (animCard != null) {
                    drawC(g, animCard, animX, animY);
                }
            }
        };
        add(p, BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton b1 = new JButton("Draw");
        JButton b4 = new JButton("Draw 10");
        JButton b2 = new JButton("Shuffle");
        JButton b3 = new JButton("Sort");

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (uiDeck.size() > 0 && animCard == null) {
                    animCard = uiDeck.drawCard();
                    animX = 50;
                    animY = 50;

                    int targetX = 200 + (uiDrawn.size() % 10) * 50;
                    int targetY = 50 + (uiDrawn.size() / 10) * 110;

                    t = new javax.swing.Timer(15, new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            int dx = targetX - animX;
                            int dy = targetY - animY;

                            if (Math.abs(dx) <= 2 && Math.abs(dy) <= 2) {
                                animX = targetX;
                                animY = targetY;
                                uiDrawn.add(animCard);
                                animCard = null;
                                t.stop();
                            } else {
                                animX += dx / 8 + Integer.signum(dx) * 2;
                                animY += dy / 8 + Integer.signum(dy) * 2;
                            }
                            p.repaint();
                        }
                    });
                    t.start();
                }
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (animCard == null) {
                    for (int i = 0; i < 10 && uiDeck.size() > 0; i++) {
                        uiDrawn.add(uiDeck.drawCard());
                    }
                    p.repaint();
                }
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uiDeck = new Deck();
                uiDeck.shuffle();
                uiDrawn.clear();
                p.repaint();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Comparator<Card> cardComparator_4 = Comparator.comparing((Card card) -> card.getSuit().getValue())
                        .thenComparing(card -> card.getRank().getValue());
                Collections.sort(uiDrawn, cardComparator_4);
                p.repaint();
            }
        });

        bot.add(b1);
        bot.add(b4);
        bot.add(b2);
        bot.add(b3);
        add(bot, BorderLayout.SOUTH);
    }

    void drawC(Graphics g, Card c, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, 70, 100, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, 70, 100, 10, 10);

        String suitStr = "";
        switch (c.getSuit()) {
            case HEARTS:
                suitStr = "H";
                g.setColor(Color.RED);
                break;
            case DIAMONDS:
                suitStr = "D";
                g.setColor(Color.RED);
                break;
            case SPADES:
                suitStr = "S";
                g.setColor(Color.BLACK);
                break;
            case CLUBS:
                suitStr = "C";
                g.setColor(Color.BLACK);
                break;
        }

        String rankStr = "";
        switch (c.getRank()) {
            case ACE:
                rankStr = "A";
                break;
            case JACK:
                rankStr = "J";
                break;
            case QUEEN:
                rankStr = "Q";
                break;
            case KING:
                rankStr = "K";
                break;
            default:
                rankStr = String.valueOf(c.getRank().getValue());
                break;
        }

        g.drawString(rankStr, x + 5, y + 15);
        g.drawString(suitStr, x + 5, y + 30);
        g.drawString(rankStr, x + 55, y + 85);
        g.drawString(suitStr, x + 55, y + 95);
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            drawnCards.add(deck.drawCard());
        }
        Comparator<Card> cardComparator_4 = Comparator.comparing((Card card) -> card.getSuit().getValue())
                .thenComparing(card -> card.getRank().getValue());
        Collections.sort(drawnCards, cardComparator_4);
        System.out.println("Drawn cards (sorted):");
        for (Card card : drawnCards) {
            System.out.println(card);
        }

        new Main().setVisible(true);
    }
}
