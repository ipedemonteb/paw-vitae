import AvailabilityComponent from "@/components/AvailabilityComponent.tsx";
import UnavailabilityComponent from "@/components/UnavailabilityComponent.tsx";

function DoctorAvailability() {
  return (
      <div className="space-y-8">
        <AvailabilityComponent/>
        <UnavailabilityComponent/>
      </div>
  )
}

export default DoctorAvailability;