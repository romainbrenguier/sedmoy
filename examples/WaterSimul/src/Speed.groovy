import java.time.Duration
import java.util.concurrent.TimeUnit

class Speed {
    double inMeterPerSeconds;

    static Speed perSecond(Length l) {
        def speed = new Speed()
        speed.inMeterPerSeconds = l.inMeter;
        return speed
    }

    Length times(Duration d) {
        Length.ofMeter(
                (inMeterPerSeconds * d.toNanos()) / TimeUnit.SECONDS.toNanos(1l))
    }
}
