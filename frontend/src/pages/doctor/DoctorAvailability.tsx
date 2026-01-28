import AvailabilityComponent from "@/components/AvailabilityComponent.tsx";
import Unavailability from "@/components/Unavailability.tsx";

function DoctorAvailability() {
    return (
        <div className="space-y-8">
            <AvailabilityComponent/>
            <Unavailability/>
        </div>
    )
}

export default DoctorAvailability;