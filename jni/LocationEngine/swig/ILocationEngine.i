/* ============= ILocationEngine ================ */
/*
 * Swig File for ILocationEngine Structure
 *
 *
*/		

%template(VectorOfPublicBeacon) std::vector<Innerpeacer::BLELocationEngine::IPXPublicBeacon>;
typedef PublicBeaconList std::vector<Innerpeacer::BLELocationEngine::IPXPublicBeacon>;
%template(VectorOfScannedBeaconPointer) std::vector<const Innerpeacer::BLELocationEngine::IPXScannedBeacon *>;
typedef ScannedBeaconPointerList std::vector<const Innerpeacer::BLELocationEngine::IPXScannedBeacon *>;
namespace Innerpeacer {
    namespace BLELocationEngine {
        class ILocationEngine {
        public:
            ILocationEngine() {};
            virtual void Initilize(const std::vector<IPXPublicBeacon> &beacons, std::string checkCode) = 0;
            virtual void processBeacons(std::vector<const IPXScannedBeacon *> &beacons) = 0;
            virtual void addStepEvent() = 0;
			virtual void reset() = 0;
            virtual IPXPoint getLocation() const = 0;
			virtual IPXPoint getImmediateLocation() const = 0;
            virtual ~ILocationEngine() {};
        };
    }
}

Innerpeacer::BLELocationEngine::ILocationEngine *CreateIPXStepBaseTriangulationEngine(IPXAlgorithmType type);
