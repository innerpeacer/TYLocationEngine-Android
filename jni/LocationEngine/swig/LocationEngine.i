/* ============= ILocationEngine ================ */
/*
 * Swig File for ILocationEngine Structure
 *
 *
*/		

%template(VectorOfPublicBeacon) std::vector<Nephogram::BLELocationEngine::NPXPublicBeacon>;
typedef PublicBeaconList std::vector<Nephogram::BLELocationEngine::NPXPublicBeacon>;
%template(VectorOfScannedBeaconPointer) std::vector<const Nephogram::BLELocationEngine::NPXScannedBeacon *>;
typedef ScannedBeaconPointerList std::vector<const Nephogram::BLELocationEngine::NPXScannedBeacon *>;
namespace Nephogram {
    namespace BLELocationEngine {
        class ILocationEngine {
        public:
            ILocationEngine() {};
            virtual void Initilize(const std::vector<NPXPublicBeacon> &beacons) = 0;
            virtual void processBeacons(std::vector<const NPXScannedBeacon *> &beacons) = 0;
            virtual void addStepEvent() = 0;
			virtual void reset() = 0;
            virtual NPXPoint getLocation() const = 0;
            virtual ~ILocationEngine() {};
        };
    }
}

Nephogram::BLELocationEngine::ILocationEngine *CreateNPXStepBaseTriangulationEngine(NPXAlgorithmType type);
