package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Community {
    BANGALI("Bangali"),
    MARMA("Marma"),
    CHAKMA("Chakma"),
    TANCHANGYA("Tanchangya"),
    CHAK("Chak"),
    MURONG("Murong"),
    BAWM("Bawm"),
    TRIPURA("Tripura"),
    SANTAL("Santal"),
    KHUMI("Khumi"),
    KHYANG("Khyang"),
    GARO("Garo"),
    LUSHAI("Lushai"),
    PANGKHUA("Pangkhua"),
    KHASIA("Khasia"),
    BUNA("Buna"),
    HAJONG("Hajong"),
    HORIZON("Horizon"),
    KOCH("Koch"),
    KUKI("Kuki"),
    MAHATU("Mahatu"),
    MANDI("Mandi"),
    MANIPURI("Manipuri"),
    MIZO("Mizo"),
    MUNDA("Munda"),
    ORAON("Oraon"),
    PAHARIA("Paharia"),
    RAJBONGSHI("Rajbongshi"),
    RAKHAIN("Rakhain"),
    URUA("Urua"),
    TELUGU("Telugu"),
    PAHAN("Pahan");

    private final String title;
}
